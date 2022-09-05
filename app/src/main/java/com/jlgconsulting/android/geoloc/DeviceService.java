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
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();

        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        di.deviceName = Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
        di.imeiList = new ArrayList<>();
        int count = tm.getPhoneCount();
        for (int i = 0; i < count; i++) {
            di.imeiList.add(tm.getImei(i));
        }

        di.model = Build.MODEL;
        di.display = Build.DISPLAY;
        di.uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        di.systemName = "Android";
        di.systemVersion = Build.VERSION.RELEASE;
        di.deviceId = Build.BOARD;
        di.readableVersion = "1.0.1";
        try {
            di.macAddress = wInfo.getMacAddress();
        } catch (SecurityException e) {
            Log.e(TAG, "e = " + e);
        }
        String serialNumber = Build.getSerial();
        if (serialNumber == null) {
            serialNumber = "unknown";
        }
        di.serialNumber = serialNumber;
        di.manufacturer = Build.MANUFACTURER;

        return di;
    }
}
