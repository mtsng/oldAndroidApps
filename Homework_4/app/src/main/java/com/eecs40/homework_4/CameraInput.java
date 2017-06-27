package com.eecs40.homework_4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Michael on 5/27/2015.
 */
public class CameraInput {
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    private Context context;
    private MainActivity main;
    private String mCurrentPhotoPath;

    public CameraInput(Context context, MainActivity main){
        this.context = context;
        this.main = main;
    }

    public void dispatchTakePictureIntent () {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }
            catch(IOException ex){
                Toast.makeText(main, "Image Not Saved", Toast.LENGTH_LONG).show();
            }
            if(photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                //return intent to main onactivityresult
                main.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                galleryAddPic();
            }
        } else {
            Toast.makeText(main, "Error Camera not Detected", Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException{
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        //System.out.println("  THIS   "+Environment.getExternalStorageDirectory().getAbsolutePath());
        //Save a file path for use
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        main.sendBroadcast(mediaScanIntent);
    }

    public String getPhotoPath(){
        return mCurrentPhotoPath;
    }

}
