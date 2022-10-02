package com.akram.huaweihelper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdsApiInstance {


    public static AdsService service;

    public static AdsService getInstance(String base) {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(AdsService.class);
        }
        return service;
    }

}
