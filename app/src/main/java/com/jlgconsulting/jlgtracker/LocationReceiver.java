package com.jlgconsulting.jlgtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jlgconsulting.android.geoloc.parcelable.GeolocInfo;

public class LocationReceiver extends BroadcastReceiver {

    final private static String TAG = "JLGModule.LocationReceiver";

    public static final String LOCATION_INTENT = "com.jlgconsulting.android.geoloc.event.LOCATION_INTENT";

    final private RunningActivity runningActivity;

    LocationReceiver(RunningActivity runningActivity) {
        this.runningActivity = runningActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "LocationReceiver: onReceive broadcast.");
        GeolocInfo geolocInfo = intent.getParcelableExtra("geolocInfo");
        if (geolocInfo != null) {
            this.runningActivity.setGeolocInfo(geolocInfo);
        }
    }
}