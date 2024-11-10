package com.example.appchat.providers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface CloudinaryService {
    @Multipart
    @POST("image/upload")
    Call<CloudinaryResponse> uploadImage(
            @Part MultipartBody.Part file,
            @Query("api_key") String apiKey,
            @Query("timestamp") long timestamp,
            @Query("signature") String signature,
            @Query("upload_preset") String uploadPreset
    );
}

