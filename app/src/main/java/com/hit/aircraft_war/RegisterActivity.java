package com.hit.aircraft_war;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {


    private String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etEmail = findViewById(R.id.register_emailAddress);
        EditText etPassword = findViewById(R.id.register_password);
        EditText etConfirm = findViewById(R.id.register_confirmPassword);

        Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            finish();
        });
    }
}