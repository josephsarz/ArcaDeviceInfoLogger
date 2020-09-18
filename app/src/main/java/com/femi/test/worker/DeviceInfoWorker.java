package com.femi.test.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.femi.test.model.UserDeviceInfo;
import com.femi.test.network.ApiUtils;


public class DeviceInfoWorker extends Worker {

    private String TAG = "DeviceInfoWorker";

    public DeviceInfoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        Context context = getApplicationContext();
        try {
            UserDeviceInfo userDeviceInfo = DeviceInfoScheduler.getLogDetails(context);
            Log.i(TAG, "sending logs... "+userDeviceInfo.toString());
            ApiUtils.sendDeviceInfo(userDeviceInfo);
        }catch (Exception e){
            Log.i(TAG, "failed to send logs"+e);
        }
        return Worker.Result.success();
    }


}
