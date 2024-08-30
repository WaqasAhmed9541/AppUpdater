package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.appupdater.databinding.ActivityTestMobileBinding;

import java.util.ArrayList;
import java.util.List;

public class TestMobileActivity extends AppCompatActivity {
ActivityTestMobileBinding binding;
    private TestMobileAdapter adapter;
    List<ModelClassForInfo> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTestMobileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green));
        }

        binding.recycler.setLayoutManager(new LinearLayoutManager(TestMobileActivity.this));
       itemList.add(new ModelClassForInfo("Ear Speaker",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Micro Phone",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Multi Touch",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Finger Print",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Flash Light",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Display",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Loud Speaker",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Ear Proximity",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Volume Up",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Volume Down",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Accelerometer",R.drawable.baseline_install_mobile_24));
       itemList.add(new ModelClassForInfo("Light Sensor",R.drawable.baseline_install_mobile_24));
        adapter = new TestMobileAdapter(TestMobileActivity.this,itemList);
        binding.recycler.setAdapter(adapter);


    }
}