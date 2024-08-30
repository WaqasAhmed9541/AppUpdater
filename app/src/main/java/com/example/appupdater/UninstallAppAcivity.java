package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.appupdater.databinding.ActivityUninstallAppAcivityBinding;
import com.google.android.material.tabs.TabLayout;

public class UninstallAppAcivity extends AppCompatActivity {
    ActivityUninstallAppAcivityBinding binding;
    int user=0;
    TabLayout tabLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUninstallAppAcivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        tabLayout = findViewById(R.id.user);
        tabLayout.addTab(tabLayout.newTab().setText("User App"));
        tabLayout.addTab(tabLayout.newTab().setText("System App"));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recycler, new UserAppFragment())
                    .commit();
        }

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new UserAppFragment();
                        break;
                    case 1:
                        selectedFragment = new SystemAppFragment();
                        break;

                }

                // Replace the current fragment with the selected one
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recycler, selectedFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed here
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed here
            }
        });



    }


}