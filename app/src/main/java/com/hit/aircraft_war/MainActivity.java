package com.hit.aircraft_war;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hit.aircraft_war.controller.ActivityController;

public class MainActivity extends AppCompatActivity {

    /**难度标志
     * 0简单，1普通，2困难
     * */
    public static int difficultChoice;

    public static int WIDTH;
    public static int HEIGHT;
    public int  m;

    public  static boolean bgmFlag;

    private String name;

    //获取屏幕宽高
    public void getScreenHW(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口宽度与宽度
        WIDTH = dm.widthPixels;
        HEIGHT = dm.heightPixels;

    }


    private Switch s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenHW();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        s = (Switch) findViewById(R.id.soundSwitch);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //选中时 do some thing
                    bgmFlag=true;
                    Toast.makeText(MainActivity.this,"音效开启", Toast.LENGTH_SHORT).show();
                } else {
                    //非选中时 do some thing
                    bgmFlag=false;
                    Toast.makeText(MainActivity.this,"音效关闭", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.soundSwitch).setOnClickListener(v -> {

            bgmFlag =true;
        });

        Intent lastIntent = getIntent();
        name = lastIntent.getStringExtra("userName");

        findViewById(R.id.main_easyButton).setOnClickListener(v -> {
            //联网与等待
            if (SelectActivity.network) {
                connectNetwork();
            }
            //开始游戏
            difficultChoice = 0;
            startGame();
        });

        findViewById(R.id.main_mediumButton).setOnClickListener(v -> {
            //联网与等待
            if (SelectActivity.network) {
                connectNetwork();
            }
            //开始游戏
            difficultChoice = 1;
            startGame();
        });

        findViewById(R.id.main_hardButton).setOnClickListener(v -> {
            //联网与等待
            if (SelectActivity.network) {
                connectNetwork();
            }
            //开始游戏
            difficultChoice = 2;
            startGame();
        });
    }

    public void startGame(){
        ActivityController.finishAll();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("userName", name);
        startActivity(intent);
        finish();
    }

    public void connectNetwork(){
        //Todo
    }
}