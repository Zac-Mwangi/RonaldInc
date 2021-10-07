package com.example.ronaldinc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.ronaldinc.extras.ApiClient;

public class ReportView extends AppCompatActivity {

    private ProgressDialog progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);

        WebView webView = findViewById(R.id.myReportView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(ReportView.this, "Reports", "Generating...");

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                //Log.i(TAG, "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Log.e(TAG, "Error: " + description);
                Toast.makeText(ReportView.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", (dialog, which) -> {
                    return;
                });
                alertDialog.show();
            }
        });

        Intent intent = getIntent();
        String requestUrl = intent.getStringExtra("requestUrl");
        //webView.loadUrl(AppConstants.reportsUrl+requestUrl);
        webView.loadUrl(ApiClient.reportsUrl+requestUrl);
        Log.e("Url", ApiClient.reportsUrl+requestUrl);


    }
    }