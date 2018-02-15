package com.safe.sgeor.safedriving;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class HomeFragment extends Fragment implements SensorEventListener {
    public static final String TITLE = "Home";

    //sensor stuff
    private SensorManager sensorManager;
    private Sensor accelerometer;
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
        sensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        //get a list of available sensors
        //deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.d("SENSOR", String.valueOf(event.values[0]));
        double[] gravityV = new double[3];
        double x, y, z;

        final float alpha = 0.8f;
        //gravity is calculated here
        gravityV[0] = alpha * gravityV[0] + (1 - alpha) * event.values[0];
        gravityV[1] = alpha * gravityV[1] + (1 - alpha)* event.values[1];
        gravityV[2] = alpha * gravityV[2] + (1 - alpha) * event.values[2];
        //acceleration retrieved from the event and the gravity is removed
        x = event.values[0] - gravityV[0];
        y = event.values[1] - gravityV[1];
        z = event.values[2] - gravityV[2];

        Log.d("X: ", String.valueOf(x));
        Log.d("Y: ", String.valueOf(y));
        Log.d("Z: ", String.valueOf(z));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onStart() {
        super.onStart();

        if(this.getUserVisibleHint()) {
            this.registerSensorListener();
        }
    }

    private void registerSensorListener() {
        sensorManager.registerListener(this,
                sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

}
