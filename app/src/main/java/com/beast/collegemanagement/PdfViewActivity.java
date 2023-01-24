package com.beast.collegemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import javax.xml.transform.sax.SAXResult;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

         WebView webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent();
        String pdf = intent.getStringExtra("link");

        webview.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

    }
}