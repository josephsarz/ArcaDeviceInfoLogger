package com.femi.test.network;

import com.femi.test.model.UserDeviceInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @POST("logStatus")
    @Headers("Content-Type: application/json")
    Call<Void> sendDeviceInfo(@Header("key") String token, @Body UserDeviceInfo userDeviceInfo);
}
