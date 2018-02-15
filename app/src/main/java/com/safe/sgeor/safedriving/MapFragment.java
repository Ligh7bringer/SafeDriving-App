package com.safe.sgeor.safedriving;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    public static final String TITLE = "Map";

    //map fragment to display the map
    private SupportMapFragment supportMapFragment;
    //fused location... to get locations
    private FusedLocationProviderClient fusedLocationProviderClient;
    //stuff needed to draw route
    private ArrayList<LatLng> points; //added
    Polyline line; //added

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        View parent = inflater.inflate(R.layout.fragment_map, container, false);

        //initialise
        points = new ArrayList<LatLng>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        //get map fragment
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);

        //if it exists
        if (supportMapFragment == null) {
            //initialise it
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, supportMapFragment).commit();
        }

        if (supportMapFragment != null) {
            //get map
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                //when it's ready
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null) {
                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        //set up a listener and move camera to current location
                        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    LatLng currentPos = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(15.0f).build();
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                    googleMap.addMarker(new MarkerOptions().title("You are here").position(currentPos));
                                    googleMap.moveCamera(cameraUpdate);

                                    points.add(currentPos);
                                    redrawLine(googleMap);
                                }
                            }
                        });
                    }
                }
            });
        }

        return parent;
    }

    private void redrawLine(GoogleMap googleMap){
        googleMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        //googleMap.addMarker(new ); //add Marker in current position
        line = googleMap.addPolyline(options); //add Polyline
    }

}
