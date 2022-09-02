package com.jlgconsulting.jlgtracker;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        String htmlText = "<p>This app sends your geoloc to a URL every D seconds.</p>" +
                "<p>You can configure the URL and the delay D.</p>";
        textView.setText(Html.fromHtml(htmlText, FROM_HTML_MODE_COMPACT));


    }
}