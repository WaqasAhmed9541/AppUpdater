package com.example.appupdater;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appupdater.databinding.ActivityExtraFilesBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExtraFilesActivity extends AppCompatActivity implements OnItemClickListener{
ActivityExtraFilesBinding binding;
    private ProgressBar progressBar;
    List<File> systemExtraFiles=new ArrayList<>();
    List<File> appExtraFiles=new ArrayList<>();
    List<File> emptyFolders=new ArrayList<>();
    private static final int MANAGE_EXTERNAL_CODE = 200;
    List<File> apkFiles=new ArrayList<>();
    List<ModelClassForextraFile> list=new ArrayList<>();
    private boolean isScanning = false;

    private ScanExtraFileAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityExtraFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView=findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);
binding.scanNow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            View decor = getWindow().getDecorView();
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (checkper12()) {

                startscan();
            } else {
                ActivityCompat.requestPermissions(ExtraFilesActivity.this, new String[]
                        {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, MANAGE_EXTERNAL_CODE);
            }
        } else {

            if (Environment.isExternalStorageManager()) {

                startscan();
            } else {
                requestManageExternalStoragePermission();
            }

        }



    }
});
        insertlistdata();

        adapter = new ScanExtraFileAdapter(list,ExtraFilesActivity.this,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ExtraFilesActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == MANAGE_EXTERNAL_CODE) {
            if (grantResults.length > 0) {
                boolean checkread = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (checkread) {
                    startscan();
                } else {
                    Toast.makeText(this, "permision denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean checkper12() {
        int read = ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        return read == PackageManager.PERMISSION_GRANTED;

    }
    private void requestManageExternalStoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
        startActivityForResult(intent, MANAGE_EXTERNAL_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MANAGE_EXTERNAL_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()){
                    startscan();
                    Toast.makeText(this, "Permision  allow", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permision deny", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void startscan(){
        MyAsyncTask myAsyncTask = new MyAsyncTask(ExtraFilesActivity.this,binding.scantext);
        myAsyncTask.execute();
    }

    @Override
    public void onItemClick(int position) {

        if (isScanning) {
            Toast.makeText(this, "Scanning in progress, please wait...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (position == 0) {
            List<String> filePaths = convertfileIntoPath(systemExtraFiles);
            Intent intent = new Intent(ExtraFilesActivity.this, SystenExtraFileActivity.class);
            intent.putStringArrayListExtra("file_paths", new ArrayList<>(filePaths));
            startActivity(intent);
        } else if (position == 1) {
            List<String> filePaths = convertfileIntoPath(appExtraFiles);
            Intent intent = new Intent(ExtraFilesActivity.this, AppExtraFileActivity.class);
            intent.putStringArrayListExtra("file_paths", new ArrayList<>(filePaths));
            startActivity(intent);
        } else if (position == 2) {
            List<String> filePaths = convertfileIntoPath(emptyFolders);
            Intent intent = new Intent(ExtraFilesActivity.this, EmptyFolderActivity.class);
            intent.putStringArrayListExtra("file_paths", new ArrayList<>(filePaths));
            startActivity(intent);
        } else if (position == 3) {
            List<String> filePaths = convertfileIntoPath(apkFiles);
            Intent intent = new Intent(ExtraFilesActivity.this, AppApkFileActivity.class);
            intent.putStringArrayListExtra("file_paths", new ArrayList<>(filePaths));
            startActivity(intent);
        }
    }

    public List<String> convertfileIntoPath(List<File> systemExtraFiles){
        List<String> filePaths = new ArrayList<>();
        for (File file : systemExtraFiles) {
            filePaths.add(file.getAbsolutePath());
        }
        return filePaths;
    }

    private class MyAsyncTask extends AsyncTask<Void, String, Void> {
        private Context context;
        private TextView textView;
        private Handler handler;

        public MyAsyncTask(Context context, TextView textView) {
            this.context = context;
            this.textView = textView;
            this.handler = new Handler(Looper.getMainLooper());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isScanning = true;
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FileScanner fileScanner = new FileScanner(context);


            fileScanner.setFileScanListener(new FileScanner.FileScanListener() {
                @Override
                public void onFileScanned(File file) {

                    handler.post(() -> textView.setText("Scanning: " + file.getAbsolutePath()));
                }
            });

            // Start scanning the external storage
            fileScanner.scanExternalStorage();

            // Update the lists after scanning
            systemExtraFiles = fileScanner.getSystemExtraFiles();
            appExtraFiles = fileScanner.getAppExtraFiles();
            emptyFolders = fileScanner.getEmptyFolders();
            apkFiles = fileScanner.getApkFiles();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isScanning = false;
            insertlistdata();
            adapter.notifyDataSetChanged();
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    public  void insertlistdata(){
    list.clear();
    list.add(new ModelClassForextraFile("System  extra Files",R.drawable.baseline_install_mobile_24,systemExtraFiles));
    list.add(new ModelClassForextraFile("App extra file",R.drawable.baseline_install_mobile_24,appExtraFiles));
    list.add(new ModelClassForextraFile("Empty folder",R.drawable.baseline_install_mobile_24,emptyFolders));
    list.add(new ModelClassForextraFile("App Apk",R.drawable.baseline_install_mobile_24,apkFiles));

}


}