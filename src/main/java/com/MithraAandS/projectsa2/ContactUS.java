package com.MithraAandS.projectsa2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ContactUS extends AppCompatActivity {
    private Toolbar contactusToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        contactusToolbar = findViewById(R.id.contactusus_toolbar);
        setSupportActionBar(contactusToolbar);
        getSupportActionBar().setTitle("Product Description");
    }
}
