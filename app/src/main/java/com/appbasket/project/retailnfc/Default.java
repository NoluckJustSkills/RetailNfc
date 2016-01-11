package com.appbasket.project.retailnfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Default extends AppCompatActivity {
TextView tvStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        tvStatus = (TextView) findViewById(R.id.tvStatus);

        Intent intent = getIntent();

        String status = intent.getStringExtra("status");
        tvStatus.setText(status);
    }


}
