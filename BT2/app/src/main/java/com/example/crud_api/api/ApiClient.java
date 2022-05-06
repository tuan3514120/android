package com.example.crud_api.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
// Kết nối với localhost
    private static final String BASE_URL = "http://192.168.1.10/DEMO_INTERNET/";
    private static Retrofit retrofit;
    public static Retrofit getApiClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
