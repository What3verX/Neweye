package com.osmanlioglu.neweye;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.osmanlioglu.neweye.databinding.ActivityPrivacyBinding;
import com.osmanlioglu.neweye.databinding.ActivityPrivacyBinding;

public class PrivacyActivity extends AppCompatActivity {

    ActivityPrivacyBinding binding;

    WebView webView;
    Activity activity;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.backArrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivacyActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        activity = this;
        progressDialog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progressDialog.setCancelable(false);

        webView = findViewById(R.id.webview_privacy_policy);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);




        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                progressDialog.show();
                view.loadUrl(url);
                return true;
            }

            @Override public void onPageFinished(WebView view,final String url){
                progressDialog.dismiss();
            }

        });
        webView.loadUrl("https://www.freeprivacypolicy.com/live/314c2ffc-be51-4460-9b09-f3483433cf32");
    }
}


