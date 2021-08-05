package com.muhammadyaseenfatimamazharsarfarz.signatureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));



        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork() {
        for (int progress = 0; progress < 100; progress += 10) {
            try {
                Thread.sleep(100);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void startApp() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}