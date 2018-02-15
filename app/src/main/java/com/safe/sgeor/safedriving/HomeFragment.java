package com.safe.sgeor.safedriving;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TITLE = "Home";

    //sensor stuff
    private SensorManager sensorManager;
    private List<Sensor> deviceSensors;
    private TextView sensors, tvSpeed;
    private EditText etSpeedLimit;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        //set up sensor manager
        //sensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        //get a list of available sensors
        //deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        //get gui stuff
        tvSpeed = v.findViewById(R.id.txtCurrentSpeed);
        etSpeedLimit = v.findViewById(R.id.txtSpeedLimit);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the gps location provider.
               //Log.d("SPEED: ", String.valueOf(location.getSpeed()));
               int speed =(int) ((location.getSpeed()*3600)/1000);
               tvSpeed.setText(String.valueOf(speed) + " miles/h");

               //check if over speed limit
               String value = etSpeedLimit.getText().toString();
               if(speed > Integer.parseInt(value)) {
                   v.setBackgroundColor(Color.argb(255, 255, 51, 51));
               } else {
                   v.setBackgroundColor(Color.argb(255, 51, 255, 51));
               }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        }

        //get text view
       // sensors = v.findViewById(R.id.textSensors);

        //display sensors
//        for(Sensor _s : deviceSensors) {
//            Log.d("SENSOR: ", _s.getName());
//            String name = _s.getName();
//            sensors.setText(sensors.getText() + name + "\n");
//        }

        return v;
    }

    private void makeUseOfNewLocation(Location location) {
        tvSpeed.setText(String.valueOf(location.getSpeed()));
    }
}
