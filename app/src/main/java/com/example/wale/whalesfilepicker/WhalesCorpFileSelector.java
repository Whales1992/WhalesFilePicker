package com.example.wale.whalesfilepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class WhalesCorpFileSelector extends AppCompatActivity {

    private WhalesCorpFileSelectorAdapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<String> filesPaths = new ArrayList<>();

    private JSONArray obj = new JSONArray();

    private String recipient;

    private JSONArray paths = new JSONArray();
    private String folder_title = "";
    private int count = 0;

    private String file_type ="";

    private JSONArray selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whalescorpfileselector);

        recyclerView = findViewById(R.id.recyclerView);

        try{
            Intent intent = getIntent();
            file_type = intent.getStringExtra("type");
            recipient = intent.getStringExtra("title");
            Utility.initToolbarNew("Send to "+ recipient, false,  "", this);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        int numberOfColumns = 2;
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        adapter = new WhalesCorpFileSelectorAdapter(this, obj, file_type, recipient);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        switch (file_type){
            case "image":
                filesPaths.addAll(Utility.getImagesPath(this));
                break;

            case "video":
                filesPaths.addAll(Utility.getVideosPath(this));
                break;

            case "audio":
                filesPaths.addAll(Utility.getAudiosPath(this));
                break;

            case "document":
                Utility uti = new Utility();
                filesPaths.addAll(uti.getDocumentsPath());
                break;
        }

        analiseFiles();
    }

    private void analiseFiles() {
        try {
            for (int x = 0; x< filesPaths.size(); x++) {
                File file = new File(filesPaths.get(x));

                String[] var = file.getParent().split("/");
                for (int a = 0; a < var.length; a++) {

                    if (a == (var.length - 1)) {
                        String name = var[a]+"";

                        if(folder_title.equals(name)){

                            //********HERE WE'RE CAPTURING THE MATCH DATA'S SO THAT WE CAN GROUP THEM EASILY AND KEEP COUNT OF HOW MANY THEY ARE****
                            count+=1;
                            paths.put(file.getPath());

                            //******HERE WE'RE SAVING THE LAST DATA IN THE LIST*****
                            if(x==(filesPaths.size()-1)){
                                saveItem();
                            }
                        }else{
                            //******HERE WE'RE SAVING THE PREVIOUS DATA BEFORE OVERRIDING IT WITH THE NEW SET OF DATA TO BE COLLECTED*****
                            if(x==(filesPaths.size()-1)){
                                count+=1;
                                folder_title = name;
                                paths.put(file.getPath());

                                saveItem();
                            }else if(count!=0){
                                saveItem();
                            }

                            //*******HERE WE'RE OVERRIDING THE PREVIOUS DATA THAT HAS BEEN SAVE ABOVE******
                            count=1;
                            folder_title = name;
                            paths.put(file.getPath());

                        }

                    }
                }
            }

            Log.e("TOTAL SIZE", obj.length() + " @");

            adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveItem(){
        try{
            JSONObject data = new JSONObject();
            data.put("folder", folder_title);
            data.put("count", count);
            data.put("paths", paths);

            obj.put(data);

            paths = new JSONArray();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == 000111 && resultCode == RESULT_OK) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<JSONArray>() {}.getType();
                    selected = gson.fromJson(data.getStringExtra("data"), type);
                    file_type = data.getStringExtra("type");

                    setResult(RESULT_OK, data);
                    finish();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}