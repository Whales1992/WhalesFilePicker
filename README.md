# WhalesFilePicker
A very basic and straight forward android medias picking library, videos, audios and documents from both sd-card and internal memory


HOW TO USE

Dont't forget to request read and add the below line to your manifest
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

                Intent intent = new Intent(this, WhalesCorpFileSelector.class);
                intent.putExtra("title", "Select a folder"); //this is the title that will be shown on the toolbar
                intent.putExtra("type", "image"); //meaning you want to select images only...rest like ("video"), ("audio") and ("document")
                startActivityForResult(intent, REQUEST_CODE); // You can as well use 000111
                
                
HOW TO GET RESULT OF SELECTED FILES

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (resultCode == RESULT_OK) {
     
                if (requestCode == REQUEST_CODE) { // or 000111
                 Gson gson = new Gson();
                        Type type = new TypeToken<JSONArray>() {}.getType();
                        JSONArray selected = gson.fromJson(data.getStringExtra("data"), type);
                        String file_type = data.getStringExtra("type");// This returns the file type e.g "image", "video", "audio", "document"
                        
                        //NOW YOU CAN DO WHAT EVER YOU WANT WITH IT
                }
        }
    }
