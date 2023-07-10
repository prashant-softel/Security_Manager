package com.example.securitymanager.securitymanager;

/**
 * Created by delaroystudios on 10/5/2016.
 */
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppConfig {

    public static String BASE_URL = "https://way2society.com/";

    public static Retrofit getRetrofit() {
        System.out.println("Upload Image" +BASE_URL);
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
