package com.hit.aircraft_war;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hit.aircraft_war.controller.ActivityController;
import com.hit.aircraft_war.store.User;

import org.litepal.LitePal;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //获取信息
        TextView showEmail = findViewById(R.id.profile_showEmail);
        TextView showPassword = findViewById(R.id.profile_showPassword);
        TextView showName = findViewById(R.id.profile_showName);

        Intent lastIntent = getIntent();
        String email = lastIntent.getStringExtra("userEmail");
        String password = lastIntent.getStringExtra("userPassword");

        List<User> users = LitePal.where("userEmail = ?", email).find(User.class);
        String name = users.get(0).getUserName();

        showEmail.setText("邮  箱："+email);
        showPassword.setText("密  码："+password);
        showName.setText("用户名："+name);

        //按键事件
        Button resignBtn = findViewById(R.id.profile_resignButton);
        Button deleteBtn = findViewById(R.id.profile_deleteButton);

        resignBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("提示");
            builder.setMessage("是否退出登录");
            builder.setPositiveButton("确定",(dialog, which) -> {
                Toast.makeText(this, "退出成功", Toast.LENGTH_SHORT).show();
                //关闭上一个标签页
                ActivityController.finishAll();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("取消", null);

            builder.show();

        });

        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("警告");
            builder.setMessage("是否确定注销？");
            builder.setPositiveButton("确定",(dialog, which) -> {
                LitePal.deleteAll(User.class, "userEmail=?", email);
                Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
                //关闭上一个标签页
                ActivityController.finishAll();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("取消", null);

            builder.show();

        });
    }
}