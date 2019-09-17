package com.example.cameraapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static int REQ_1 = 1;
    private static int REQ_2 = 2;
    private ImageView mImageView;
    private String mFilePath;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mImageView = (ImageView) findViewById(R.id.showimage);
        mImageView = findViewById(R.id.showimage);
//        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = getFilesDir().getPath();
        mFilePath = mFilePath + "/" + "temp.png";


    }

    public void startCamera(View view){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_1);
    }

    public void showCameraRes(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri photoUri = Uri.fromFile(new File(mFilePath));
        Log.d(TAG, "mFilePath: "+ mFilePath);
        Uri photoUri =  FileProvider.getUriForFile(
                MainActivity.this,
                "com.example.cameraapp.example.provider", //(use your app signature + ".provider" )
                new File(mFilePath));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQ_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQ_1){
                Bundle bundle= data.getExtras();
                Bitmap bitmap= (Bitmap) bundle.get("data");
                mImageView.setImageBitmap(bitmap);
            } else if (requestCode == REQ_2) {
                FileInputStream fis =null;
                try {
                    fis = new FileInputStream(mFilePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    mImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
