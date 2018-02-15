package com.safe.sgeor.safedriving;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TITLE = "Home";

    //sensor stuff
    private SensorManager sensorManager;
    private List<Sensor> deviceSensors;
    private TextView sensors;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        //set up sensor manager
        sensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        //get a list of available sensors
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        //get text view
        sensors = v.findViewById(R.id.textSensors);

        //display sensors
        for(Sensor _s : deviceSensors) {
            Log.d("SENSOR: ", _s.getName());
            String name = _s.getName();
            sensors.setText(sensors.getText() + name + "\n");
        }

        return v;
    }

}
