package com.jlgconsulting.jlgtracker;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        String htmlText = "<p>This app sends your geoloc to a URL every D seconds.</p>" +
                "<p>You can configure the URL and the delay D.</p>";
        textView.setText(Html.fromHtml(htmlText, FROM_HTML_MODE_COMPACT));

        continueButton = findViewById(R.id.button);
    }

    public void gotoNextActivity(View view) {
        Log.d("JLGModule", "click");
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}