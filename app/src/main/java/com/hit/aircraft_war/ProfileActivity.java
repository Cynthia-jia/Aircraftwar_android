package com.hit.aircraft_war;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView showEmail;
    private TextView showPassword;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        showEmail = findViewById(R.id.profile_showEmail);
        showPassword = findViewById(R.id.profile_showPassword);

        Intent lastIntent = getIntent();
        String email = lastIntent.getStringExtra("userEmail");
        String password = lastIntent.getStringExtra("userPassword");

        showEmail.setText("邮 箱："+email);
        showPassword.setText("密 码："+password);
    }
}