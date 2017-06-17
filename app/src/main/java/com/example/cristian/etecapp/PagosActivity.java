package com.example.cristian.etecapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PagosActivity extends AppCompatActivity {
    private TextView finalAmount;
    private int amount=0;
    private String strAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       finalAmount = (TextView)findViewById(R.id.finalAmount);
        for (int i = 0; i < SampleMaterialActivity.cardsSelected.size();i++){
            amount += Integer.parseInt(SampleMaterialActivity.cardsSelected.get(i).getPrice());
        }
        strAmount = "" + amount;
        finalAmount.setText(strAmount);



    }
}
