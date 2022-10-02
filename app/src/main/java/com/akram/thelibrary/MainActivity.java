package com.akram.thelibrary;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.akram.huaweihelper.HwHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        HwHelper.showInterOnDemand(this);


    }
}