package com.example.wale.whalesfilepicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;

public class WhalesCorpFileSelectorAdapter extends RecyclerView.Adapter<WhalesCorpFileSelectorAdapter.ViewHolder>{
    private LayoutInflater mInflater;

    private Activity activity;

    private String recipient;

    private String file_type, folder_title;

    private JSONArray data;

    public WhalesCorpFileSelectorAdapter(Activity activity, JSONArray data, String file_type, String recipient) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;

        this.data = data;
        this.file_type = file_type;
        this.recipient = recipient;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_grouped_files_list, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            final JSONObject grouped_files = data.getJSONObject(position);

            folder_title = grouped_files.getString("folder");
            holder.folder_title.setText(folder_title);
            holder.images_count.setText(grouped_files.getInt("count")+"");

            final JSONArray array = grouped_files.getJSONArray("paths");

            setViewType(holder);

            switch (file_type){
                case "image":
                    doForImage(array, holder);
                    break;

                case "video":
                    doForVideo(array, holder);
                    break;

                case "audio":
//                    doForAudio(array, holder);
                    break;

                case "document":
//                    doForDocument(array, holder);
                    break;
            }

            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), WhalesCorpFilesView.class);
                    Gson gson = new Gson();
                    Type type = new TypeToken<JSONArray>(){}.getType();
                    String data = gson.toJson(array, type);
                    intent.putExtra("data", data);
                    intent.putExtra("type", file_type);
                    intent.putExtra("folder", holder.folder_title.getText().toString());
                    activity.startActivityForResult(intent, 000111);
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
        ImageView imageView, documentView, audioView;
        TextView folder_title, images_count;
        VideoView videoView;
        RelativeLayout parent_layout;

        ViewHolder(View itemView) {
            super(itemView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            documentView = itemView.findViewById(R.id.documentView);
            audioView = itemView.findViewById(R.id.audioView);
            imageView = itemView.findViewById(R.id.imageView);
            videoView = itemView.findViewById(R.id.videoView);
            folder_title = itemView.findViewById(R.id.folder_title);
            images_count = itemView.findViewById(R.id.images_count);
        }
    }


    private void setViewType(ViewHolder holder){
        switch (file_type){
            case "image":
                holder.videoView.setVisibility(View.GONE);
                holder.audioView.setVisibility(View.GONE);
                holder.documentView.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                break;

            case "video":
                holder.imageView.setVisibility(View.GONE);
                holder.audioView.setVisibility(View.GONE);
                holder.documentView.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.VISIBLE);
                break;

            case "audio":
                holder.imageView.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.GONE);
                holder.documentView.setVisibility(View.GONE);
                holder.audioView.setVisibility(View.VISIBLE);
                break;

            case "document":
                holder.imageView.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.GONE);
                holder.audioView.setVisibility(View.GONE);
                holder.documentView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void doForImage(JSONArray array, final ViewHolder holder){
        try{
            File file = new File(array.getString(0));

            Picasso.get().load(file).centerCrop().fit().into(holder.imageView, new Callback() {
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
    private void doForVideo(final JSONArray array, final ViewHolder holder) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... avoid) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            File file = new File(array.getString(0));
                            holder.videoView.setVideoPath(file.getAbsolutePath());
                            holder.videoView.seekTo(1);
                            holder.videoView.start();

//                        final Handler handler = new Handler();
//                        handler.postDelayed(() -> {
//                            try {
//                                holder.videoView.stopPlayback();
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }, 500);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
                return null;
            }
        }.execute();
    }

    private void doForAudio(JSONArray array, ViewHolder holder){

    }

    private void doForDocument(JSONArray array, ViewHolder holder){

    }
}