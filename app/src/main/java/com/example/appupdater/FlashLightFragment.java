package com.example.appupdater;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlashLightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashLightFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "MainActivity";
    private static final int CAMERA_PERMISSION_REQUEST_CODE
            = 100;
    private CameraManager cameraManager;
    private String cameraId;

    private boolean isFlashOn;

    public FlashLightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FlashLightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FlashLightFragment newInstance(String param1, String param2) {
        FlashLightFragment fragment = new FlashLightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flash_light, container, false);

        Button buttonFlash = view.findViewById(R.id.test);
        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        buttonFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlashlight();
                } else {
                    turnOnFlashlight();
                }
            }
        });

        return view;
    }

    private void turnOnFlashlight() {
        try {
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId, true);
                isFlashOn = true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlashlight() {
        try {
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId, false);
                isFlashOn = false;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    }




