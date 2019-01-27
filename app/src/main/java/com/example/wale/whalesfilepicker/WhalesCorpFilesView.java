package com.example.wale.whalesfilepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;

public class WhalesCorpFilesView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static WhalesCorpFilesViewAdapter adapter;
    private JSONArray data = new JSONArray();
    private static String folder_title;

    private static ImageButton menu_icon;
    private static LinearLayout action_button;
    private static RelativeLayout backbtn;
    private static TextView btn_next, toolbartitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whales_corp_files_view);

        backbtn = findViewById(R.id.backbtn);
        action_button = findViewById(R.id.action_button);
        menu_icon = action_button.findViewById(R.id.menu_icon);
        btn_next = action_button.findViewById(R.id.btn_next);
        toolbartitle = findViewById(R.id.toolbartitle);

        recyclerView = findViewById(R.id.recyclerView);
        int numberOfColumns = 3;
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        Intent intent = getIntent();
        Gson gson = new Gson();
        Type type = new TypeToken<JSONArray>() {}.getType();
        data = gson.fromJson(intent.getStringExtra("data"), type);

        String file_type = intent.getStringExtra("type");
        folder_title = intent.getStringExtra("folder");

        Utility.initToolbarNew(folder_title, true, "", this);
        menu_icon.setVisibility(View.VISIBLE);
        menu_icon.setBackgroundResource(R.drawable.check);


        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.determineSelectionMode(true);

                menu_icon.setVisibility(View.GONE);
                btn_next.setVisibility(View.VISIBLE);

                btn_next.setText("Next");
                increaseSelected(0);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.next();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_next.getVisibility() == View.VISIBLE){
                    resetToolbarState();
                    WhalesCorpFilesViewAdapter.resetSelected();
                    adapter.determineSelectionMode(false);
                }else{
                    adapter.determineSelectionMode(false);
                    finish();
                }
            }
        });

        adapter = new WhalesCorpFilesViewAdapter(this, data, file_type);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public static void increaseSelected(int count){
        toolbartitle.setText(count+" Selected");
    }

    public static void setToolbarState(int count){
        try{
            menu_icon.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);

            btn_next.setText("Next");
            increaseSelected(count);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void resetToolbarState(){
            try{
                adapter.determineSelectionMode(false);
                Utility.initToolbarNew(folder_title, true, "", this);
                menu_icon.setVisibility(View.VISIBLE);
                menu_icon.setBackgroundResource(R.drawable.check);
                btn_next.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    @Override
    public void onPause(){
        super.onPause();

        resetToolbarState();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        resetToolbarState();
    }
}