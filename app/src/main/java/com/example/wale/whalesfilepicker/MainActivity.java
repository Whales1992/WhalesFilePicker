package com.example.wale.whalesfilepicker;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    private Button image, vidoe, audio, document;
    private static final int REQUEST_PERMISSION_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        assureReadablePermission();

        setOnclickListener();
    }

    private void setOnclickListener() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permitted())
                    Utility.NAVIGATE(MainActivity.this, WhalesCorpFileSelector.img, getResources().getString(R.string.app_name));
            }
        });

        vidoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permitted())
                    Utility.NAVIGATE(MainActivity.this, WhalesCorpFileSelector.vid, getResources().getString(R.string.app_name));
            }
        });


        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permitted())
                    Utility.NAVIGATE(MainActivity.this, WhalesCorpFileSelector.aud, getResources().getString(R.string.app_name));
            }
        });


        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permitted())
                    Utility.NAVIGATE(MainActivity.this, WhalesCorpFileSelector.doc, getResources().getString(R.string.app_name));
            }
        });
    }

    private void findViewsById(){
        image = findViewById(R.id.picture);
        vidoe = findViewById(R.id.video);
        audio = findViewById(R.id.audio);
        document = findViewById(R.id.document);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            int permissionReadExternal = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if(permissionReadExternal != PermissionChecker.PERMISSION_GRANTED){
                Toast.makeText(this, "Read Storage Permission Denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void assureReadablePermission() {
        int permissionReadExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionReadExternal != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_STORAGE);
        }
    }

    private boolean permitted(){
        int permissionReadExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permissionReadExternal == PermissionChecker.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == Utility.REQUEST_CODE && resultCode == RESULT_OK) {
                Log.e("RESUTL", data+"");
                Log.e("RESUTL", data.getDataString()+"");
//                Gson gson = new Gson();
//                Type type = new TypeToken<JSONArray>() {}.getType();
//                selected = gson.fromJson(data.getStringExtra("data"), type);
//                file_type = data.getStringExtra("type");
//
//                finish();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}