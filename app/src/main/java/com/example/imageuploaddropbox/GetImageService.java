package com.example.imageuploaddropbox;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetImageService {
    @Headers({
            "Content-Type: application/json",
            "Authorization:Bearer Zx2a_yYmgAAAAAAAAAAAkozAakV92v0effAMAJxDf_7dBYezD6TdK9UyjqVZ-BGM",

    })
    @POST("get_temporary_link")
    Call<FileData> getTempLink(
            @Body String path
    );

}
