package com.akram.huaweihelper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AdsService {


    @GET("{path}")
    Call<CustomAdConfig> getAdsConfig(@Path("path") String ppt);

}
