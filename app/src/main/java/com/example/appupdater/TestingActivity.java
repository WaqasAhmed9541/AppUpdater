package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.appupdater.databinding.ActivityTestingBinding;

public class TestingActivity extends AppCompatActivity {
ActivityTestingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent receivedIntent = getIntent();
        String codepath = receivedIntent.getStringExtra("testType");
//        binding.test.setText(codepath);


        replaceFragment(new EarSpeakerFragment());
        switch (codepath) {
            case "Ear Speaker":
                replaceFragment(new EarSpeakerFragment());
                break;

            case "Micro Phone":
                replaceFragment(new MicroPhoneFragment());
                break;
            case "Multi Touch":
                replaceFragment(new MultiTouchFragment());
                break;
            case "Finger Print":
                replaceFragment(new FingerPrintFragment());
                break;
            case "Flash Light":
                replaceFragment(new FlashLightFragment());
                break;
            case "Display":
                replaceFragment(new DisplayFragment());
                break;
            case "Loud Speaker":
                replaceFragment(new LoudSpeakerFragment());
                break;
            case "Ear Proximity":
                replaceFragment(new EarProximityFragment());
                break;
            case "Volume Up":
                replaceFragment(new VolumeUpFragment());
                break;
            case "Volume Down":
                replaceFragment(new VolumeDownFragment());
                break;
            case "Accelerometer":
                replaceFragment(new AccelerometerFragment());
                break;
            case "Light Sensor":
                replaceFragment(new LightSensorFragment());
                break;
        }







    }

    private Fragment replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
        return fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        if (fragment instanceof VolumeUpFragment) {
            ((VolumeUpFragment) fragment).onVolumeKeyPressed(keyCode);
        } else if (fragment instanceof VolumeDownFragment) {
            ((VolumeDownFragment) fragment).onVolumeKeyPressed(keyCode);
        }

        return super.onKeyDown(keyCode, event);
    }

}