package com.hit.aircraft_war;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hit.aircraft_war.controller.ActivityController;
import com.hit.aircraft_war.store.User;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private ImageView displayPassword;
    private boolean isHide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityController.addActivity(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Button loginBtn = findViewById(R.id.login_button);
        Button registerBtn = findViewById(R.id.reg_button);
        displayPassword = findViewById(R.id.password_imageView);
        etEmail = findViewById(R.id.login_emailAddress);
        etPassword = findViewById(R.id.login_password);

        loginBtn.setOnClickListener(v -> {
            login();
        });

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        //设置密码的可见与不可见
        displayPassword.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        displayPassword.setOnClickListener(v -> {
            if (isHide) {
                displayPassword.setImageResource(R.drawable.ic_baseline_visibility_24);
                HideReturnsTransformationMethod visibleMethod = HideReturnsTransformationMethod.getInstance();
                etPassword.setTransformationMethod(visibleMethod);
                isHide = false;
            }else {
                displayPassword.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                TransformationMethod invisibleMethod = PasswordTransformationMethod.getInstance();
                etPassword.setTransformationMethod(invisibleMethod);
                isHide = true;
            }
            int index = etPassword.getText().toString().length();
            etPassword.setSelection(index);
        });
    }

    public void login() {
        if (isUserEmailAndPwdValid()) {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            List<User> userList = LitePal.where("userEmail = ? and userPassword = ?",email,password).find(User.class);
            if (userList.size()>0){//登陆成功
                Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
                intent.putExtra("userEmail",email);
                intent.putExtra("userPassword",password);
                Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }else {//登陆失败
                Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isUserEmailAndPwdValid() {
        String userEmail = etEmail.getText().toString();
        String userPassword = etPassword.getText().toString();
        if (userEmail.equals("")) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (userPassword.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}