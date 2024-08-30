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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccelerometerFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView xAxisTextView;
    private TextView yAxisTextView;
    private TextView zAxisTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accelerometer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        xAxisTextView = view.findViewById(R.id.x_axis);
        yAxisTextView = view.findViewById(R.id.y_axis);
        zAxisTextView = view.findViewById(R.id.z_axis);

        // Initialize the SensorManager and the accelerometer
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Check if the device has an accelerometer
        if (accelerometer == null) {
            xAxisTextView.setText("No accelerometer available");
            yAxisTextView.setText("");
            zAxisTextView.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the accelerometer sensor listener
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the accelerometer sensor listener
        if (accelerometer != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Get the accelerometer values
            float xAxis = event.values[0];
            float yAxis = event.values[1];
            float zAxis = event.values[2];

            int xAxisInt = (int) xAxis;
            int yAxisInt = (int) yAxis;
            int zAxisInt = (int) zAxis;

// Display the accelerometer values
            xAxisTextView.setText("X Axis: " + xAxisInt);
            yAxisTextView.setText("Y Axis: " + yAxisInt);
            zAxisTextView.setText("Z Axis: " + zAxisInt);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can handle sensor accuracy changes here if needed
    }
}
