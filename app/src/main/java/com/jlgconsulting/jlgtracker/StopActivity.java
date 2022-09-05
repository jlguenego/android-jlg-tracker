package com.jlgconsulting.jlgtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
    }

    public void onStopClick(View view) {
        Log.d("JLGModule", "stop click");
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}