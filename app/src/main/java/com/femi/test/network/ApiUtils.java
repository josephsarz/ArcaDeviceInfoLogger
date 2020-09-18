package com.femi.test.network;

import android.util.Log;

import com.femi.test.BuildConfig;
import com.femi.test.model.UserDeviceInfo;


import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtils {

    private ApiUtils() {}

    private static String TAG = "ApiUtils";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(APIService.class);
    }

    public static void sendDeviceInfo(UserDeviceInfo userDeviceInfo) {

      ApiUtils.getAPIService().sendDeviceInfo(BuildConfig.API_KEY,userDeviceInfo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, "Log submitted to API.");
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e(TAG, "Unable to submit logs to API. "+t.getMessage());
            }
        });

    }


}
