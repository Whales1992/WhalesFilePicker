package com.example.wale.whalesfilepicker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Utility {
    public static void initToolbarNew(String title, boolean with_right_menu, String right_menu_title, final Activity activity){
        TextView toolbarTitle = activity.findViewById(R.id.toolbartitle);
        RelativeLayout backbtn = activity.findViewById(R.id.backbtn);
        ImageButton menu_icon = activity.findViewById(R.id.menu_icon);
        TextView btn_next = activity.findViewById(R.id.btn_next);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                activity.overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
            }
        });

        toolbarTitle.setText(title);

        if(with_right_menu){
            if(right_menu_title.equals("")) {
                menu_icon.setVisibility(View.VISIBLE);
            }else{
                btn_next.setVisibility(View.VISIBLE);
                btn_next.setText(right_menu_title);
            }
        }
    }

    private ArrayList<String> filesList = new ArrayList<>();

    public static String getFileSize(String filePath){
            File file = new File(filePath);
            int kilobyte = Integer.parseInt(String.valueOf(file.length()/1024));

            if(kilobyte>1000)
                return getMegabyte(kilobyte);
            else
                return kilobyte+"KB";
    }

    private static String getMegabyte(int kilobyte){
        return kilobyte/1000+"MB";
    }

    public static ArrayList<String> getImagesPath(Activity activity)
    {
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = activity.managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {
//                    if(imagePath.isDirectory())
//                    {
//                        imageList = imagePath.listFiles();
//
//                    }
                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                            )
                    {
                        String path= imagePath.getAbsolutePath();

                        if(!imagePath.getName().startsWith(".")){
                            resultIAV.add(path);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultIAV;
    }

    public static ArrayList<String> getVideosPath(Activity activity)
    {
        Uri u = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<String> resultIAV = new ArrayList<>();

        String[] directories = null;
        if (u != null)
        {
            c = activity.managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File videoDir = null;
            if (directories != null) {
                videoDir = new File(directories[i]);
            }
            File[] videoList = videoDir.listFiles();
            if(videoList == null)
                continue;
            for (File videoPath : videoList) {
                try {
//                    if(videoPath.isDirectory())
//                    {
//                        videoList = videoPath.listFiles();
//                    }

                    //***************IF ONLY WE WANT TO RESTRICT SOME SPECIFIC FORMATS
                    if ( videoPath.getName().contains(".mp4")|| videoPath.getName().contains(".MP4")
                            || videoPath.getName().contains(".avi")|| videoPath.getName().contains(".AVI")
                            || videoPath.getName().contains(".3gp") || videoPath.getName().contains(".3GP")
                            || videoPath.getName().contains(".wmv") || videoPath.getName().contains(".WMV")
                            || videoPath.getName().contains(".mov") || videoPath.getName().contains(".MOV")
                            || videoPath.getName().contains(".m4p") || videoPath.getName().contains(".M4P")
                            || videoPath.getName().contains(".m4v") || videoPath.getName().contains(".M4V")
                            || videoPath.getName().contains(".mpg") || videoPath.getName().contains(".MPG")
                            || videoPath.getName().contains(".mpeg") || videoPath.getName().contains(".MPEG")
                            || videoPath.getName().contains(".m2v") || videoPath.getName().contains(".M2V")
                            || videoPath.getName().contains(".flv") || videoPath.getName().contains(".FLV")
                            || videoPath.getName().contains(".f4v") || videoPath.getName().contains(".F4V")
                            || videoPath.getName().contains(".mkv") || videoPath.getName().contains(".MKV")
                            || videoPath.getName().contains(".webm") || videoPath.getName().contains(".WEBM")
                            || videoPath.getName().contains(".vob") || videoPath.getName().contains(".VOB")
                            || videoPath.getName().contains(".ogv") || videoPath.getName().contains(".OGV")
                            || videoPath.getName().contains(".ogg") || videoPath.getName().contains(".OGG")
                            )
                    {
                        String path= videoPath.getAbsolutePath();

                        if(!videoPath.getName().startsWith(".")){
                            resultIAV.add(path);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultIAV;
    }

    public static ArrayList<String> getAudiosPath(Activity activity)
    {
        Uri u = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<>();
        ArrayList<String> resultIAV = new ArrayList<>();

        String[] directories = null;
        if (u != null)
        {
            c = activity.managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File audioDir = null;
            if (directories != null) {
                audioDir = new File(directories[i]);
            }
            File[] audioList = audioDir.listFiles();
            if(audioList == null)
                continue;
            for (File audioPath : audioList) {
                try {
//                    if(audioPath.isDirectory())
//                    {
//                        audioList = audioPath.listFiles();
//                    }

                    //***************IF ONLY WE WANT TO RESTRICT SOME SPECIFIC FORMATS
                    if ( audioPath.getName().contains(".wma")|| audioPath.getName().contains(".WMA")
                            || audioPath.getName().contains(".mp3")|| audioPath.getName().contains(".MP3")
                            || audioPath.getName().contains(".wav")|| audioPath.getName().contains(".WAV")
                            || audioPath.getName().contains(".ogg")|| audioPath.getName().contains(".OGG")
                            || audioPath.getName().contains(".oga")|| audioPath.getName().contains(".OGA")
                            || audioPath.getName().contains(".mogg")|| audioPath.getName().contains(".MOGG")
                            || audioPath.getName().contains(".opus")|| audioPath.getName().contains(".OPUS")
                            || audioPath.getName().contains(".raw")|| audioPath.getName().contains(".RAW")
                            || audioPath.getName().contains(".vox")|| audioPath.getName().contains(".VOX")
                            || audioPath.getName().contains(".aa")|| audioPath.getName().contains(".AA")
                            || audioPath.getName().contains(".aac") || audioPath.getName().contains(".AAC")
                            || audioPath.getName().contains(".aax") || audioPath.getName().contains(".AAX")
                            || audioPath.getName().contains(".amr") || audioPath.getName().contains(".AMR")
                            || audioPath.getName().contains(".flac") || audioPath.getName().contains(".FLAC")
                            || audioPath.getName().contains(".m4a") || audioPath.getName().contains(".M4A")
                            )
                    {
                        String path= audioPath.getAbsolutePath();

                        if(!audioPath.getName().startsWith(".")){
                            resultIAV.add(path);
                        }

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultIAV;
    }

    public ArrayList<String> getDocumentsPath()
    {
        File root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());

        return getfile(root);
    }

    private ArrayList<String> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
//                    fileList.add(listFile[i]);
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".pdf") || listFile[i].getName().endsWith(".PDF")
                            || listFile[i].getName().endsWith(".doc") || listFile[i].getName().endsWith(".DOC")
                            || listFile[i].getName().endsWith(".docx") || listFile[i].getName().endsWith(".DOCX")
                            || listFile[i].getName().endsWith(".odt") || listFile[i].getName().endsWith(".ODT")
                            || listFile[i].getName().endsWith(".xls") || listFile[i].getName().endsWith(".XLS")
                            || listFile[i].getName().endsWith(".xlsx") || listFile[i].getName().endsWith(".XLSX")
                            || listFile[i].getName().endsWith(".ods") || listFile[i].getName().endsWith(".ODS")
                            || listFile[i].getName().endsWith(".pptx") || listFile[i].getName().endsWith(".PPTX")
                            || listFile[i].getName().endsWith(".txt") || listFile[i].getName().endsWith(".TXT")
                            || listFile[i].getName().endsWith(".ppt") || listFile[i].getName().endsWith(".PPT")
                            || listFile[i].getName().endsWith(".dat") || listFile[i].getName().endsWith(".DAT")
                            || listFile[i].getName().endsWith(".enc") || listFile[i].getName().endsWith(".ENC")
                            || listFile[i].getName().endsWith(".html") || listFile[i].getName().endsWith(".HTML")
                            || listFile[i].getName().endsWith(".htm") || listFile[i].getName().endsWith(".HTM")
                            || listFile[i].getName().endsWith(".mhtml") || listFile[i].getName().endsWith(".MHTML")
                            || listFile[i].getName().endsWith(".vcf") || listFile[i].getName().endsWith(".VCF"))
                    {
//                        fileList.add(listFile[i]);
                        if(!listFile[i].getName().startsWith(".")){
                            filesList.add(listFile[i].getAbsolutePath());
                        }
                    }
                }

            }
        }
        return filesList;
    }

    public static Uri getImageContentUri(Context context, File imageFile)
    {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Uri getVideoContentUri(Context context, File videoFile)
    {
        String filePath = videoFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Video.Media._ID },
                MediaStore.Video.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (videoFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Uri getAudioContentUri(Context context, File audioFile)
    {
        String filePath = audioFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media._ID },
                MediaStore.Audio.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (audioFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}

