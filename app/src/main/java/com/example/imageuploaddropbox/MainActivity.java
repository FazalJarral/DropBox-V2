package com.example.imageuploaddropbox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;

public class MainActivity extends AppCompatActivity {
    Button selectImg;
    Button uploadImg;
    ImageView img;
    private int GALLERY_REQUEST_CODE = 123;
    private String ACCESS_TOKEN = "Zx2a_yYmgAAAAAAAAAAAkozAakV92v0effAMAJxDf_7dBYezD6TdK9UyjqVZ-BGM";
    DbxClientV2 client;
    Intent mData;
    String uri_string;
    String mPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissionGranted();
        selectImg = findViewById(R.id.select);
        uploadImg = findViewById(R.id.upload);
        img = findViewById(R.id.img);
        DropboxClientFactory.init(ACCESS_TOKEN);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);

            }
        });


        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 uploadFile(uri_string);
                Log.e("URI", "onClick: " + uri_string );
            }
        });
    }

    private void checkPermissionGranted() {
        if(( ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.READ_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED ) && ( ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED )) {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
           requestPermission();
        }
        else {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE} ,123);
    }


    private void uploadFile(String uri_string) {
        new UploadFileTask(this, DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                Log.e("Complete", "onUploadComplete: " + result.toString() );
                String message = result.getName() + " size " + result.getSize() + " modified " +
                        DateFormat.getDateTimeInstance().format(result.getClientModified());
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                        .show();


                Toast.makeText(getApplicationContext(), "Successfully Uploaded.", Toast.LENGTH_SHORT)
                        .show();

                Intent i = new Intent(getApplicationContext() , GetImageActivity.class);
                    i.putExtra("path_lower" , result.getPathLower());
                    startActivity(i);
            }

            @Override
            public void onError(Exception e) {

                Log.e("ERROR ", "Failed to upload file.", e);
                Toast.makeText(MainActivity.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(uri_string, mPath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mData = data;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Uri selectedImage = data.getData();
                 uri_string = selectedImage.toString();
                 mPath = selectedImage.getPath();
                img.setImageURI(selectedImage);
                Log.e("DATA", "onActivityResult: " + data.getDataString());

            }
        }
    }


}
