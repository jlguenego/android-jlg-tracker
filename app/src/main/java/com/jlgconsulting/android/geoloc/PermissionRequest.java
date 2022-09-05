package com.jlgconsulting.android.geoloc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Map;

public class PermissionRequest {
    final private static String TAG = "JLGModule.PermissionRequest";
    final private static String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    final private AppCompatActivity activity;

    final private ActivityResultLauncher<Intent> requestBatteryActivityResult;
    private ActivityResultCallback<ActivityResult> requestBatteryActivityResultRunnable;

    final private ActivityResultLauncher<String[]> requestPermissionActivityResult;
    private ActivityResultCallback<Map<String, Boolean>> requestPermissionActivityResultRunnable;

    public static boolean areOK(AppCompatActivity activity) {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public PermissionRequest(AppCompatActivity activity) {
        this.activity = activity;

        requestBatteryActivityResult = activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result) -> {
            Log.d(TAG, "result = " + result);
            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            String packageName = activity.getPackageName();
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                requestBatteryActivityResultRunnable.onActivityResult(result);
                return;
            }
            requestBatteryActivityResultRunnable.onActivityResult(new ActivityResult(Activity.RESULT_OK, null));
        });

        requestPermissionActivityResult = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
            Log.d(TAG, "isGranted:" + isGranted);
            requestPermissionActivityResultRunnable.onActivityResult(isGranted);
        });
    }

    @SuppressLint("BatteryLife")
    public void askForBattery(ActivityResultCallback<ActivityResult> r) {
        this.requestBatteryActivityResultRunnable = r;
        Intent intent = new Intent();
        String packageName = activity.getPackageName();
        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            requestBatteryActivityResult.launch(intent);
            return;
        }
        requestBatteryActivityResultRunnable.onActivityResult(new ActivityResult(Activity.RESULT_OK, null));
    }

    public void askForPermissions(ActivityResultCallback<Map<String, Boolean>> r) {
        this.requestPermissionActivityResultRunnable = r;
        requestPermissionActivityResult.launch(PERMISSIONS);
    }

}
