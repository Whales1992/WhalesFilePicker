package com.example.wale.whalesfilepicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WhalesCorpFilesViewAdapter extends RecyclerView.Adapter<WhalesCorpFilesViewAdapter.ViewHolder>{
    private LayoutInflater mInflater;

    private static Activity activity;

    private static String file_type;

    private JSONArray data;

    private static List<String> sels = new ArrayList<>();

    private static boolean make_selectable = false;

    public WhalesCorpFilesViewAdapter(Activity activity, JSONArray data, String file_type) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;

        this.data = data;
        this.file_type = file_type;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_files_view_list, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            final String path = data.getString(position);

            setViewType(holder);

            if(make_selectable)
            {
                holder.selection_indicator.setVisibility(View.VISIBLE);
                if(sels.contains(path)){
                    holder.selection_indicator.setBackgroundResource(R.drawable.file_selected);
                }else{
                    holder.selection_indicator.setBackgroundResource(R.drawable.file_not_selected);
                }
            }
            else
            {
                holder.selection_indicator.setVisibility(View.GONE);
            }

            switch (file_type){
                case "image":
                    doForImage(path, holder);
                    break;

                case "video":
                    doForVideo(path, holder);
                    break;

                case "audio":
                    doForAudio(path, holder);
                    break;

                case "document":
                    doForDocument(path, holder);
                    break;
            }

            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!make_selectable){
                        JSONArray array = new JSONArray();
                        array.put(path);

                        Intent intent = new Intent(v.getContext(), WhalesCorpFilesView.class);
                        Gson gson = new Gson();
                        Type type = new TypeToken<JSONArray>(){}.getType();
                        String data = gson.toJson(array, type);
                        intent.putExtra("data", data);
                        intent.putExtra("type", file_type);

                        activity.setResult(activity.RESULT_OK, intent);
                        activity.finish();
                    }else{
                        if(sels.contains(path)){
                            sels.remove(path);
                            holder.selection_indicator.setBackgroundResource(R.drawable.file_not_selected);
                            WhalesCorpFilesView.increaseSelected(sels.size());
                        }else{
                            sels.add(path);
                            holder.selection_indicator.setBackgroundResource(R.drawable.file_selected);
                            WhalesCorpFilesView.increaseSelected(sels.size());
                        }
                    }
                }
            });

            holder.parent_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    make_selectable = true;

                    if(sels.contains(path)){
                        sels.remove(path);
                        WhalesCorpFilesView.setToolbarState(sels.size());
                    }else{
                        sels.add(path);
                        WhalesCorpFilesView.setToolbarState(sels.size());
                    }

                    notifyDataSetChanged();

                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*--------------------------------------------------------------------------------------
     |                          GET REFERENCES TO VIEWS HERE
     *--------------------------------------------------------------------------------------*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView file_view, audio_view, document_view;
        VideoView video_view;
        TextView file_title;
        RelativeLayout parent_layout;
        Button selection_indicator;

        ViewHolder(View itemView) {
            super(itemView);
            selection_indicator = itemView.findViewById(R.id.selection_indicator);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            file_view = itemView.findViewById(R.id.file_view);
            video_view = itemView.findViewById(R.id.video_view);
            audio_view = itemView.findViewById(R.id.audio_view);
            document_view = itemView.findViewById(R.id.document_view);

            file_title = itemView.findViewById(R.id.file_title);
        }
    }

    private void setViewType(ViewHolder holder){
        switch (file_type){
            case "image":
                holder.video_view.setVisibility(View.GONE);
                holder.audio_view.setVisibility(View.GONE);
                holder.document_view.setVisibility(View.GONE);
                holder.file_view.setVisibility(View.VISIBLE);
                break;

            case "video":
                holder.file_view.setVisibility(View.GONE);
                holder.audio_view.setVisibility(View.GONE);
                holder.document_view.setVisibility(View.GONE);
                holder.video_view.setVisibility(View.VISIBLE);
                break;

            case "audio":
                holder.file_view.setVisibility(View.GONE);
                holder.video_view.setVisibility(View.GONE);
                holder.document_view.setVisibility(View.GONE);
                holder.audio_view.setVisibility(View.VISIBLE);
                break;

            case "document":
                holder.file_view.setVisibility(View.GONE);
                holder.video_view.setVisibility(View.GONE);
                holder.audio_view.setVisibility(View.GONE);
                holder.document_view.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void doForImage(String path, ViewHolder holder){
        try{
            File file = new File(path);
                holder.file_title.setText(file.getName());
                Picasso.get().load(file).centerCrop().fit().into(holder.file_view, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void doForVideo(final String path, final ViewHolder holder){
        try
        {
            new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... avoid) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(path);
                        if(file.exists()){
                            holder.file_title.setText(file.getName());

                            holder.video_view.setVideoPath(file.getAbsolutePath());
                            holder.video_view.seekTo(5);

//                        holder.video_view.start();

//                        final Handler handler = new Handler();
//                        handler.postDelayed(() -> {
//                            try {
//                                holder.video_view.stopPlayback();
//                            }catch (Exception ex){
//                                ex.printStackTrace();
//                            }
//                        }, 500);

                        }
                    }
                });
                return null;
                }
            }.execute();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void doForAudio(String path, ViewHolder holder){
                File file = new File(path);
                holder.file_title.setText(file.getName());
    }

    @SuppressLint("StaticFieldLeak")
    private void doForDocument(final String path, final ViewHolder holder){
        new AsyncTask<Void, Void, Void>(){
                        @Override
            protected Void doInBackground(Void... avoid) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(path);
                        String name = file.getName();
                        holder.file_title.setText(name);

                        String ext = burnOutExtention(name);
                        switch (ext) {
                            case ".pdf":
                                holder.document_view.setImageResource(R.drawable.pdf);
                                break;

                            case ".docx":
                            case ".doc":
                                holder.document_view.setImageResource(R.drawable.docx);
                                break;

                            case ".html":
                                holder.document_view.setImageResource(R.drawable.html);
                                break;

                            case ".txt":
                                holder.document_view.setImageResource(R.drawable.txt);
                                break;

                            case ".ppt":
                            case ".pptx":
                                holder.document_view.setImageResource(R.drawable.ppt);
                                break;

                            case ".xls":
                            case ".xlsx":
                                holder.document_view.setImageResource(R.drawable.xls);
                                break;

                            default:
                                holder.document_view.setImageResource(R.drawable.ic_document_box);
                                break;
                        }
                    }
                });
                return null;
            }
        }.execute();
    }

    private String burnOutExtention(String file_name){
        StringBuilder ext = new StringBuilder();
        try {
            String[] splt = file_name.split("");
            int from_here = 0;
            int new_lnt;
            //Adewale Kolawole O. Curriculum.docx
            for (int i = 0; i < splt.length; ++i) {
                if (splt[i].equalsIgnoreCase(".")) {
                    from_here = i;
                }
            }

            new_lnt = splt.length - from_here;
            String[] ext_array = new String[new_lnt];
            for (int j = 0; j < new_lnt; ++j) {
                ext_array[j] = splt[from_here];
                from_here += 1;
            }

            for (String anExt_array : ext_array) {
                ext.append(anExt_array);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return ext.toString();
    }


    public void determineSelectionMode(boolean va){
        try{
            sels = new ArrayList<>();

            make_selectable = va;
            notifyDataSetChanged();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void next(){
        try{
            if(!sels.isEmpty()){
                JSONArray array = new JSONArray();
                for (String s : sels){
                    array.put(s);
                }

                Intent intent = new Intent(activity, WhalesCorpFilesView.class);
                Gson gson = new Gson();
                Type type = new TypeToken<JSONArray>(){}.getType();
                String data = gson.toJson(array, type);
                intent.putExtra("data", data);
                intent.putExtra("type", file_type);

                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }else{
                Toast.makeText(activity, "Select at least one item", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void resetSelected(){
        try{
            make_selectable = false;
            sels = new ArrayList<>();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}