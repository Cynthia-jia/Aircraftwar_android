package com.hit.aircraft_war;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Button singleBtn = findViewById(R.id.select_singleButton);
        Button doubleBtn = findViewById(R.id.select_doubleButton);

        singleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SelectActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        doubleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SelectActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}