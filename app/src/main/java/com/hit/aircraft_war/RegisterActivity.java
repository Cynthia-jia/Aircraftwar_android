package com.hit.aircraft_war;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hit.aircraft_war.controller.ActivityController;
import com.hit.aircraft_war.store.User;

import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirm;
    private Button registerBtn;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        etEmail = findViewById(R.id.register_emailAddress);
        etPassword = findViewById(R.id.register_password);
        etConfirm = findViewById(R.id.register_confirmPassword);

        registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(v -> {
            if (isUserEmailAndPwdValid()) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                //检查是否已有用户
                List<User> users = LitePal.findAll(User.class);
                for (int i=0; i<users.size(); i++) {
                    if (users.get(i).getUserEmail().equals(email)) {
                        Toast.makeText(RegisterActivity.this, "该邮箱已被注册", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //判断两次密码是否一致
                if (!password.equals(etConfirm.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                //新建用户
                User user = new User(email, password);
                boolean successful = user.save();
                if (successful) {
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                    ActivityController.finishAll();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }else {
                    Log.w(TAG, "createUserWithEmail:failed");
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isUserEmailAndPwdValid() {
        String userEmail = etEmail.getText().toString();
        String userPwd = etPassword.getText().toString();
        if (userEmail.equals("")){
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if (userPwd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}