package com.example.imageuploaddropbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GetImageActivity extends AppCompatActivity {
    GetImageService getImageService;
    final String BASE_URL = "https://api.dropboxapi.com/2/files/";
    String path; //is path_lower
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        if (getIntent() != null) {
            path = getIntent().getStringExtra("path_lower");
            Log.e("path_lower", path);
        }
        imageView = (ImageView) findViewById(R.id.imageView);

        LoadImage();
    }

    private void LoadImage() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        getImageService = retrofit.create(GetImageService.class);
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("path", path);
            Call<FileData> url = getImageService.getTempLink(paramObject.toString());
            url.enqueue(new Callback<FileData>() {
                @Override
                public void onResponse(Call<FileData> call, Response<FileData> response) {
                    if (response.isSuccessful()) {
                        Log.e("response", response.body().toString());
                        FileData fileData = response.body();
                        Log.e("link" , fileData.link);

                        Glide.with(getBaseContext()).load(fileData.link).into(imageView);
                    } else {
                        Log.e("OnResponse", response.message());
                    }
                }

                @Override
                public void onFailure(Call<FileData> call, Throwable t) {
                    Log.e("Error", t.getLocalizedMessage());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
