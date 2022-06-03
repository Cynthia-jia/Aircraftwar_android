package com.hit.aircraft_war;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.hit.aircraft_war.controller.ActivityController;

public class SelectActivity extends AppCompatActivity {

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ActivityController.addActivity(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Button singleBtn = findViewById(R.id.select_singleButton);
        Button doubleBtn = findViewById(R.id.select_doubleButton);
        Button profileBtn = findViewById(R.id.select_profileButton);

        Intent lastIntent = getIntent();
        email = lastIntent.getStringExtra("userEmail");
        password = lastIntent.getStringExtra("userPassword");

        singleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SelectActivity.this, MainActivity.class);
            startActivity(intent);
        });

        doubleBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SelectActivity.this, MainActivity.class);
            startActivity(intent);
        });

        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SelectActivity.this, ProfileActivity.class);
            intent.putExtra("userEmail",email);
            intent.putExtra("userPassword",password);
            startActivity(intent);
        });
    }
}