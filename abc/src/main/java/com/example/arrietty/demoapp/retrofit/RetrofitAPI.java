package com.example.arrietty.demoapp.retrofit;

import android.database.Observable;

import com.example.arrietty.demoapp.retrofit.model.PhoneInfo;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by asus on 2017/12/14.
 */

public interface RetrofitAPI {
/*
    @Header：header处理，不能被互相覆盖，用于修饰参数，

    //动态设置Header值
    @GET("user")
    Call<User> getUser(@Header("Authorization") String authorization)
    @Headers 用于修饰方法,用于设置多个Header值：


    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);
    */

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("users/{user}/repos")
    Call<PhoneInfo> getResult(@Path("user") String user, @Header("apikey") String apikey, @Query("phoneNum") String phoneNum);

    @POST("query")
    Call<PhoneInfo> searchPsw(@Query("ower") String ower, @Query("number") String number);
    /*
    单文件上传携带参数
     */
    @Multipart
    @POST("UploadServlet")
    Call<ResponseBody> uploadfile(@Part MultipartBody.Part photo, @Part("username") RequestBody username, @Part("password") RequestBody password);

    /*
    多文件上传携带参数
     */
    @Multipart
    @POST("UploadServlet")
    Call<ResponseBody> uploadMultifiles(@PartMap Map<String, RequestBody> params, @Part("password") RequestBody password);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("server?")
    Observable<PhoneInfo> Login(@Body RequestBody body);



}

