package com.jlgconsulting.jlgtracker;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jlgconsulting.android.geoloc.GeolocForegroundService;
import com.jlgconsulting.android.geoloc.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "JLGModule.MainActivity";

    PermissionRequest pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        String htmlText = "<p>This app sends your geoloc to a URL every D seconds.</p>" +
                "<p>You can configure the URL and the delay D.</p>";
        textView.setText(Html.fromHtml(htmlText, FROM_HTML_MODE_COMPACT));

        pr = new PermissionRequest(this);
    }

    public void gotoNextActivity(View view) {
        Log.d(TAG, "click");
        // first ask for permissions

        pr.askForBattery((result) -> {
            Log.d(TAG, "ask for battery result: " + result);
            if (result.getResultCode() != Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(),"I need battery optimization set to unrestricted.",Toast.LENGTH_LONG).show();
                return;
            }
            pr.askForPermissions((permissionResult) -> {
                Log.d(TAG, "ask for permissions: " + permissionResult);
                if (permissionResult.containsValue(false)) {
                    Toast.makeText(getApplicationContext(),"I need all permissions set to unrestricted.",Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = GeolocForegroundService.isMyMusicServiceRunning() ?
                        new Intent(this, RunningActivity.class) :
                        new Intent(this, PausedActivity.class);

                startActivity(intent);

            });

        });

    }
}