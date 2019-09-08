package com.MithraAandS.projectsa2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class TermsandConditions extends AppCompatActivity {
    private Toolbar Terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsand_conditions);

        Terms = findViewById(R.id.term_toolbar);
        setSupportActionBar(Terms);
        getSupportActionBar().setTitle("Terms and Conditions");
    }
}
