package com.example.appupdater;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MicroPhoneFragment extends Fragment {

    private static final int REQUEST_MICROPHONE_PERMISSION = 200;
    private static final int SAMPLE_RATE = 44100;

    private AudioRecord audioRecord;
    private boolean isRecording = false;

    private Button btnStart, btnStop;
    private AudioVisualizerView audioVisualizer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_micro_phone, container, false);

        btnStart = view.findViewById(R.id.btnStart);
        btnStop = view.findViewById(R.id.btnStop);
        audioVisualizer = view.findViewById(R.id.audioVisualizer);

        btnStart.setOnClickListener(v -> startCheckingMicrophone());
        btnStop.setOnClickListener(v -> stopCheckingMicrophone());

        requestMicrophonePermission();

        return view;
    }

    private void requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE_PERMISSION);
        }
    }

    private void startCheckingMicrophone() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        audioRecord.startRecording();
        isRecording = true;
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);

        new Thread(() -> {
            short[] buffer = new short[bufferSize];
            while (isRecording) {
                int read = audioRecord.read(buffer, 0, bufferSize);
                if (read > 0) {
                    int maxAmplitude = 0;
                    for (int i = 0; i < read; i++) {
                        maxAmplitude = Math.max(maxAmplitude, Math.abs(buffer[i]));
                    }
                    float normalizedAmplitude = maxAmplitude / (float) 32768;
                    requireActivity().runOnUiThread(() -> audioVisualizer.addAmplitude(normalizedAmplitude));
                }
            }
        }).start();

        Toast.makeText(getContext(), "Microphone check started", Toast.LENGTH_SHORT).show();
    }

    private void stopCheckingMicrophone() {
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            Toast.makeText(getContext(), "Microphone check stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MICROPHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {

                Toast.makeText(getContext(), "Microphone permission is required", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }
    }
}
