package com.example.appupdater;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EarProximityFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView proximityStatusTextView;
    private WindowManager.LayoutParams layoutParams;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ear_proximity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        proximityStatusTextView = view.findViewById(R.id.proximity_status);

        // Initialize the SensorManager and the proximity sensor
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // Check if the device has a proximity sensor
        if (proximitySensor == null) {
            proximityStatusTextView.setText("No proximity sensor available");
        }

        // Get WindowManager.LayoutParams for adjusting brightness
        layoutParams = getActivity().getWindow().getAttributes();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the proximity sensor listener
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the proximity sensor listener
        if (proximitySensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];

            // Display status and manage screen brightness based on proximity
            if (distance < proximitySensor.getMaximumRange()) {
                proximityStatusTextView.setText("Proximity Detected");
                setWindowBrightness(0.0001f); // Dim the screen brightness
            } else {
                proximityStatusTextView.setText("No Proximity Detected ");
                setWindowBrightness(1.0f); // Restore the screen brightness
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle sensor accuracy changes if needed
    }

    private void setWindowBrightness(float brightness) {
        layoutParams.screenBrightness = brightness;
        getActivity().getWindow().setAttributes(layoutParams);
    }
}
