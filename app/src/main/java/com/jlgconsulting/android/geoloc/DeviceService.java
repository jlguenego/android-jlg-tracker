package com.jlgconsulting.android.geoloc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jlgconsulting.android.geoloc.parcelable.DeviceInfo;

import java.util.ArrayList;

public class DeviceService {
    final static private String TAG = "JLGModule.DeviceService";

    @SuppressLint("HardwareIds")
    public DeviceInfo getDeviceInfo(Context context) {
        DeviceInfo di = new DeviceInfo();

        di.deviceName = Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
        di.model = Build.MODEL;
        di.display = Build.DISPLAY;
        di.uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        di.systemName = "Android";
        di.systemVersion = Build.VERSION.RELEASE;
        di.deviceId = Build.BOARD;
        di.readableVersion = "1.0.1";
        di.manufacturer = Build.MANUFACTURER;

        return di;
    }
}
