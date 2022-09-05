package com.jlgconsulting.jlgtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onStartClick(View view) {
        Log.d("JLGModule", "On Start Click");
        // start the foreground service

        Intent intent = new Intent(this, StopActivity.class);
        startActivity(intent);
    }
}