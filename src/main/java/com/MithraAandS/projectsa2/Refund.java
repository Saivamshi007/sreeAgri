package com.MithraAandS.projectsa2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Refund extends AppCompatActivity {
    private Toolbar refundToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);

        refundToolbar = findViewById(R.id.refund_toolbar);
        setSupportActionBar(refundToolbar);
        getSupportActionBar().setTitle("Cancellation/Refund Policies:");
    }
}
