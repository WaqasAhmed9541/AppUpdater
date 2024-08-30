package com.example.appupdater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmptyFolderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<File> emptyfolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_folder);

        recyclerView=findViewById(R.id.recycler_v);
        List<String> filePaths = getIntent().getStringArrayListExtra("file_paths");
        emptyfolder=convertPathToFile(filePaths);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(emptyfolder, this);
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