package com.osmanlioglu.neweye;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.osmanlioglu.neweye.databinding.ActivityAboutBinding;
import com.osmanlioglu.neweye.databinding.ActivityPrivacyBinding;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding binding;

    WebView webView;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.backArrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}