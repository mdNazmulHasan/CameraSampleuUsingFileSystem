package com.example.user.cameraexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView photoPathTV;
    private File photoDirectory,mediaFile;
    private Uri photoUri;
    private String imageDirectoryName,imageFileName;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.myImage);
        photoPathTV = (TextView) findViewById(R.id.photoPathTV);
        photoDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //photoDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),imageDirectoryName);
        if(!photoDirectory.exists()){
            if(!photoDirectory.mkdir()){
                //Log.e(imageDirectoryName, "failed to create "+imageDirectoryName);
                Log.e("Failure", "Failed to create directory ");
            }
        }
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            photoFile = createImageFile();
            if(photoFile != null){
                photoUri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,IMAGE_REQUEST);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST){
            if(resultCode == RESULT_OK){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(photoUri.getPath(),options);
                imageView.setImageBitmap(bitmap);
                photoPathTV.setText(photoUri.getPath());
            }
        }
    }
    private File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_"+timeStamp+".jpg";
        mediaFile = new File(photoDirectory.getPath(),imageFileName);
        return mediaFile;
    }
}
