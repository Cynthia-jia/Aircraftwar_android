package com.hit.aircraft_war;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hit.aircraft_war.controller.ActivityController;
import com.hit.aircraft_war.store.User;
import com.hit.aircraft_war.store.divide.EasyTable;
import com.hit.aircraft_war.store.divide.HardTable;
import com.hit.aircraft_war.store.divide.MediumTable;

import org.litepal.LitePal;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private String email;
    private String password;
    private String userName;

    private TextView showName;
    private TextView showEasy;
    private TextView showMedium;
    private TextView showHard;

    private Handler mHandler;
    public static boolean isChanged = false;
    public static String newName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //显示信息
        initText();

        //按键事件
        Button resignBtn = findViewById(R.id.profile_resignButton);
        Button deleteBtn = findViewById(R.id.profile_deleteButton);
        Button changeBtn = findViewById(R.id.profile_changeButton);
        Button historyBtn = findViewById(R.id.profile_deleteHistory);
        //退出登录
        resignBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("提示");
            builder.setMessage("是否确定退出？");
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
        //注销账号
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("警告");
            builder.setMessage("是否确定注销？（成绩会一起被删除）");
            builder.setPositiveButton("确定",(dialog, which) -> {
                LitePal.deleteAll(User.class, "userEmail=?", email);
                LitePal.deleteAll(EasyTable.class, "name=?", userName);
                LitePal.deleteAll(MediumTable.class, "name=?", userName);
                LitePal.deleteAll(HardTable.class, "name=?", userName);
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
        mHandler = new MHandler();
        //更改用户名
        changeBtn.setOnClickListener(v -> {
            final EditText inputServer = new EditText(ProfileActivity.this);
            inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("提示").setMessage("修改用户名").setView(inputServer).setNegativeButton("取消", null);

            builder.setPositiveButton("确定",(dialog, which) -> {
                newName = inputServer.getText().toString();
                //验重
                List<User> users = LitePal.findAll(User.class);
                for (int i=0; i<users.size(); i++) {
                    if (users.get(i).getUserName().equals(newName)) {
                        Toast.makeText(this, "用户名重复", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!newName.equals("")) {
                    //更改数据库
                    User user = new User();
                    user.setUserName(newName);
                    user.updateAll("userName=?", userName);
                    EasyTable easyTable = new EasyTable();
                    easyTable.setName(newName);
                    easyTable.updateAll("name=?", userName);
                    MediumTable mediumTable = new MediumTable();
                    mediumTable.setName(newName);
                    mediumTable.updateAll("name=?", userName);
                    HardTable hardTable = new HardTable();
                    hardTable.setName(newName);
                    hardTable.updateAll("name=?", userName);
                    //刷新内存
                    userName = newName;
                    //更改界面
                    Message changeMsg = Message.obtain();
                    changeMsg.what = 1;
                    mHandler.sendMessage(changeMsg);
                    //打印对话
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        });
        //删除历史记录
        historyBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("警告").setMessage("是否确定删除？");
            builder.setPositiveButton("确定",(dialog, which) -> {
                //删除历史
                LitePal.deleteAll(EasyTable.class, "name=?", userName);
                LitePal.deleteAll(MediumTable.class, "name=?", userName);
                LitePal.deleteAll(HardTable.class, "name=?", userName);
                //更改界面
                Message changeMsg = Message.obtain();
                changeMsg.what = 2;
                mHandler.sendMessage(changeMsg);
                //打印对话
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        });
    }

    @SuppressLint("SetTextI18n")
    public void initText() {
        //获取信息
        TextView showEmail = findViewById(R.id.profile_showEmail);
        TextView showPassword = findViewById(R.id.profile_showPassword);
        showName = findViewById(R.id.profile_showName);
        showEasy = findViewById(R.id.profile_easyBest);
        showMedium = findViewById(R.id.profile_mediumBest);
        showHard = findViewById(R.id.profile_hardBest);

        Intent lastIntent = getIntent();
        email = lastIntent.getStringExtra("userEmail");
        password = lastIntent.getStringExtra("userPassword");
        userName = lastIntent.getStringExtra("userName");

        showEmail.setText("邮  箱："+email);
        showPassword.setText("密  码："+password);
        showName.setText("用户名："+ userName);

        //显示历史最佳
        List<EasyTable> easyTables = LitePal.where("name = ?", userName).order("score desc").find(EasyTable.class);
        if (easyTables.size()>0) {
            showEasy.setText("简单模式："+easyTables.get(0).getScore());
        }else {
            showEasy.setText("简单模式：无记录");
        }

        List<MediumTable> mediumTables = LitePal.where("name = ?", userName).order("score desc").find(MediumTable.class);
        if (mediumTables.size()>0) {
            showMedium.setText("普通模式："+mediumTables.get(0).getScore());
        }else {
            showMedium.setText("普通模式：无记录");
        }

        List<HardTable> hardTables = LitePal.where("name = ?", userName).order("score desc").find(HardTable.class);
        if (hardTables.size()>0) {
            showHard.setText("困难模式："+hardTables.get(0).getScore());
        }else {
            showHard.setText("困难模式：无记录");
        }
    }

    class MHandler extends Handler {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message changeMsg) {
            switch (changeMsg.what) {
                case 1:
                    isChanged = true;
                    showName.setText("用户名："+newName);
                    break;
                case 2:
                    showEasy.setText("简单模式：无记录");
                    showMedium.setText("普通模式：无记录");
                    showHard.setText("困难模式：无记录");
                    break;
                default:
                    break;
            }
        }
    }
}