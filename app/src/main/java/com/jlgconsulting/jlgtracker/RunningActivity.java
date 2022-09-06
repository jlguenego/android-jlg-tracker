package com.jlgconsulting.jlgtracker;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jlgconsulting.android.geoloc.DeviceService;
import com.jlgconsulting.android.geoloc.GeolocForegroundService;
import com.jlgconsulting.android.geoloc.JSONUtils;
import com.jlgconsulting.android.geoloc.parcelable.GeolocInfo;

import org.json.JSONException;

public class RunningActivity extends AppCompatActivity {
    private static final String TAG = "JLGModule.RunningActivity";

    final LocationReceiver myReceiver = new LocationReceiver(this);

    TextView textViewJsonContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        Log.d(TAG, "onCreate");

        textViewJsonContent = findViewById(R.id.textViewJsonContent);
        textViewJsonContent.setMovementMethod(new ScrollingMovementMethod());
        startMyService();
    }

    public void onStopClick(View view) {
        Log.d("JLGModule", "stop click");
        stopMyService();
        Intent intent = new Intent(this, PausedActivity.class);
        startActivity(intent);
    }

    private void startMyService() {
        // starting the service
        Context context = getApplicationContext();
        Intent intent = new Intent(this, GeolocForegroundService.class);
        intent.putExtra("deviceInfo", new DeviceService().getDeviceInfo(this));
        Log.d(TAG, "About to start the foreground service");
        context.startForegroundService(intent);
    }

    private void stopMyService() {
        Intent serviceIntent = new Intent(this, GeolocForegroundService.class);
        stopService(serviceIntent);
    }

    public void setGeolocInfo(GeolocInfo geolocInfo) {
        Log.d(TAG, "geolocInfo received: " + geolocInfo);
        try {
            String geolocInfoJson = JSONUtils.geolocInfoToJson(geolocInfo).toString(2);
            textViewJsonContent.setText(geolocInfoJson);
        } catch (JSONException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        IntentFilter filter = new IntentFilter(LocationReceiver.LOCATION_INTENT);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myReceiver);
    }
}