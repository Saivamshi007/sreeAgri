package com.MithraAandS.projectsa2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;


public class aboutus extends AppCompatActivity  {

    private Toolbar aboutUstoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        aboutUstoolbar = (Toolbar) findViewById(R.id.Aboutus_toolbar);
        setSupportActionBar(aboutUstoolbar);
        getSupportActionBar().setTitle("About Us");


    }

}
