package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class AppApkFileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<File> appExtraFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_apk_file);

        // Initialize RecyclerView and RadioButton
       recyclerView=findViewById(R.id.recycler_v);
       List<String> filePaths = getIntent().getStringArrayListExtra("file_paths");
        appExtraFiles=convertPathToFile(filePaths);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(appExtraFiles, this);
        recyclerView.setAdapter(fileAdapter);



    }
    public List<File> convertPathToFile(List<String> filePaths) {
        List<File> fileList = new ArrayList<>();
        for (String path : filePaths) {
            File file = new File(path);
            fileList.add(file);
        }
        return fileList;
    }
}
