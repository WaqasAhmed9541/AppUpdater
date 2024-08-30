package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appupdater.databinding.ActivitySensorBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends AppCompatActivity {
    ActivitySensorBinding binding;
    private ProgressBar progressBar;
    private SensorInfoAdapter adapter;
    List<ModelClassForSensor> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.deviceName.setText(getDeviceName());
        binding.brandName.setText(getDeviceBrand());
        binding.recycler.setLayoutManager(new LinearLayoutManager(SensorActivity.this));
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public  List<ModelClassForSensor> listAllSensors(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor sensor : sensorList) {
            String sensorName = sensor.getName();
            String sensorVendor = sensor.getVendor();
            int sensorType = sensor.getType();
            itemList.add(new ModelClassForSensor(sensorName, sensorVendor,sensorType,R.drawable.baseline_install_mobile_24));
        }
        return itemList;
    }
    public class MyAsyncTask extends AsyncTask<Void, Void, List<ModelClassForSensor>> {

        public MyAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = binding.progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<ModelClassForSensor> doInBackground(Void... voids) {

            return  listAllSensors(SensorActivity.this);

        }
        @Override
        protected void onPostExecute(List<ModelClassForSensor> result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.GONE);
            adapter = new SensorInfoAdapter(SensorActivity.this,result);
            binding.recycler.setAdapter(adapter);
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model != null && model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private  String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return str;
        } else {
            return Character.toUpperCase(firstChar) + str.substring(1);
        }
    }
    public  String getDeviceBrand() {
        return Build.BRAND;
    }
}