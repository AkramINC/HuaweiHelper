package com.akram.thelibrary;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.akram.huaweihelper.HwHelper;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FrameLayout splashView = findViewById(R.id.splash_ad_view);

        HwHelper.install(
                this,
                MainActivity.class,
                splashView,
                "https://raw.githubusercontent.com/AkramINC/AdsRepo/main/",
                "cons_1_auto_call_rec.json",
                R.color.purple_200
        );

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        HwHelper.onRestart();
    }

    @Override
    protected void onStop() {
        HwHelper.onStop();
        super.onStop();
    }

}