package com.jlgconsulting.jlgtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jlgconsulting.android.geoloc.Config;

import java.net.URI;
import java.net.URISyntaxException;

public class PausedActivity extends AppCompatActivity {
    private static final String TAG = "JLGModule.StartActivity";

    EditText editTextUrl;
    EditText editTextDelay;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch aSwitch;

    SoundPool soundPool;
    int miou2Id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paused);

        editTextUrl = findViewById(R.id.editTextUrl);
        editTextDelay = findViewById(R.id.editTextDelay);
        aSwitch = findViewById(R.id.switchSound);

        soundInit();
    }

    private void soundInit() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        miou2Id = soundPool.load(getApplicationContext(), R.raw.miou2, 1);
    }



    public void onStartClick(View view) {
        try {
            Log.d(TAG, "On Start Click");
            // start the foreground service
            String url = editTextUrl.getText().toString();
            URI geolocURI = new URI(url);
            URI testURI = geolocURI.resolve("../test");

            int delay = Integer.parseInt(editTextDelay.getText().toString()) * 1000;
            Log.d(TAG, "delay = " + delay);

            Config config = Config.getInstance();
            config.setGeolocUrl(url);
            config.setTestUrl(testURI.toString());
            config.setDelay(delay);
            config.setBroadCastUri(LocationReceiver.LOCATION_INTENT);
            config.setNotificationIcon(R.mipmap.ic_launcher);
            config.setNotificationTitle("JLG The Super Tracker");
            config.setDebugMode(aSwitch.isChecked());
            config.setNotificationText("I am flicking you... ;)");
            config.setNotificationTicker("yes...");
            config.setSound(R.raw.miou2);
            config.setActivityClass(MainActivity.class);
            config.setAuthenticationToken("my_authentication_token123!");

            Intent intent = new Intent(this, RunningActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            if (e instanceof URISyntaxException) {
                Toast.makeText(getApplicationContext(), "url malformed.", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(getApplicationContext(), "technical error.", Toast.LENGTH_LONG).show();
        }
    }
}