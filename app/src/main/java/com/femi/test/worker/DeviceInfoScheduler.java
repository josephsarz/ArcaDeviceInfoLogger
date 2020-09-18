package com.femi.test.worker;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.an.deviceinfo.device.model.Battery;
import com.an.deviceinfo.device.model.Device;
import com.an.deviceinfo.device.model.Network;
import com.an.deviceinfo.location.DeviceLocation;
import com.an.deviceinfo.location.LocationInfo;
import com.an.deviceinfo.userapps.UserAppInfo;
import com.an.deviceinfo.userapps.UserApps;
import com.femi.test.model.Location;
import com.femi.test.model.UserDeviceInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.WIFI_SERVICE;

public class DeviceInfoScheduler {

    private static String TAG = DeviceInfoScheduler.class.getSimpleName();

    public static void setupPeriodicWork(Context context) {

        Constraints myConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        PeriodicWorkRequest refreshCpnWork =
                new PeriodicWorkRequest.Builder(DeviceInfoWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(myConstraints)
                        .build();

        WorkManager.getInstance(context).enqueue(refreshCpnWork);
    }

    public static UserDeviceInfo getLogDetails(Context context) {

        Battery battery = new Battery(context);
        int batteryLevel = battery.getBatteryPercent();

        Network network = new Network(context);
        String networkType = network.getNetworkType();

        LocationInfo locationInfo = new LocationInfo(context);
        DeviceLocation location = locationInfo.getLocation();
        String longitude = "";
        if (location.getLongitude() != null) {
            longitude = String.valueOf(location.getLongitude());
        }
        String latitude = "";
        if (location.getLatitude() != null) {
            latitude = String.valueOf(location.getLatitude());
        }

        UserAppInfo userAppInfo = new UserAppInfo(context);
        List<UserApps> userApps = userAppInfo.getInstalledApps(true);
        int totalApps = userApps.size();


        boolean hasGoogleMap = false;
        UserApps googleMap = null;
        for (UserApps userApp : userApps) {
            if (userApp.getPackageName().equalsIgnoreCase("com.google.android.apps.maps")) {
                hasGoogleMap = true;
                googleMap = userApp;
            }
        }

        String googleMapVersion = "";
        if (hasGoogleMap) {
            googleMapVersion = googleMap.getVersionName();
            Log.e(TAG, " google map version: " + googleMap.getVersionName());
        }

        Device device = new Device(context);
        String serialNumber = device.getSerial();
        if (serialNumber.equals(Build.UNKNOWN)) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    serialNumber = Build.getSerial();
                } else {
                    serialNumber = Build.SERIAL;
                }
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.d("error", String.valueOf(e.getMessage()));
            }
        }

        String dataUsageCollationTime = getCurrentDateTime();

        String connectivityStatus = "";
        if (isNetworkAvailable(context)) {
            connectivityStatus = "connected";
        } else {
            connectivityStatus = "not connected";
        }

        int signalStrength = signalStrength(context);


        return new UserDeviceInfo(
                dataUsageCollationTime, googleMapVersion, hasGoogleMap, signalStrength,
                totalApps, new Location(latitude, longitude), serialNumber, networkType, connectivityStatus, batteryLevel
        );
    }


    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_status", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_status", "" + e.getMessage());
                }
            }
        }
        Log.i("update_status", "Network is available : FALSE ");
        return false;
    }

    public static int signalStrength(Context context) {
        int signalStrength = 0;
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            signalStrength = wifiInfo.getLinkSpeed();
        }catch (Exception e){
            Log.e(TAG, String.valueOf(e.getMessage()));
        }
        return signalStrength;
    }

    public static String getCurrentDateTime(){
        Date today;
        String output;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("en_US"));
        today = new Date();
        output = formatter.format(today);
        return output;
    }


}
