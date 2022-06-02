package com.hit.aircraft_war;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.aircraft_war.Store.RankAdapter;
import com.hit.aircraft_war.Store.RankDao;
import com.hit.aircraft_war.Store.RankDaoImpl;
import com.hit.aircraft_war.Store.RankMember;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    public static int newSCore;

    private RecyclerView recyclerView;
    private RankAdapter rankAdapter;

    private List<RankMember> dataList;

    private RankDao rankDao;
    private String dateString;
    private String name;

    private Path path;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //初始化数据源，并保存数据到本地
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //构建recyclerView
        initRecyclerView();

        Button button1 = findViewById(R.id.rank_inputButton);
        button1.setOnClickListener(V -> {
            buttonAction();
        });

    }

    @SuppressLint("SdCardPath")
    private void initData() throws IOException {
        rankDao = new RankDaoImpl();

        if (MainActivity.difficultChoice == 0){
            file = new File("/data/data/com.hit.aircraft_war/files", "easyRank.txt");
            path = Paths.get("/data/data/com.hit.aircraft_war/files/easyRank.txt");
        }else if (MainActivity.difficultChoice == 1){
            file = new File("/data/data/com.hit.aircraft_war/files", "mediumRank.txt");
            path = Paths.get("/data/data/com.hit.aircraft_war/files/mediumRank.txt");
        }else {
            file = new File("/data/data/com.hit.aircraft_war/files", "hardRank.txt");
            path = Paths.get("/data/data/com.hit.aircraft_war/files/hardRank.txt");
        }

        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        //读取文件
        String lineTxt;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            int txtRank = Integer.parseInt(lineTxt);
            lineTxt = bufferedReader.readLine();
            String txtName = lineTxt;
            lineTxt = bufferedReader.readLine();
            int txtScore = Integer.parseInt(lineTxt);
            lineTxt = bufferedReader.readLine();
            String txtTime = lineTxt;
            rankDao.doAdd(new RankMember(txtRank, txtName, txtScore, txtTime));
        }

        //获取时间
        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateString = dateFormat.format(currentDate);
        //添加新数据
        RankMember newInfo = new RankMember(1, "TestPlayerName", newSCore, dateString);
        rankDao.doAdd(newInfo);
        //排序
        rankDao.doSort();

        //生成数据源
        dataList = rankDao.getAllInformation();

        //写文件
        List<String> infoList = new ArrayList<>();
        for (int i=0; i<dataList.size(); i++){
            String infoRank = String.valueOf(dataList.get(i).getRank());
            infoList.add(infoRank);
            String infoName = dataList.get(i).getName();
            infoList.add(infoName);
            String infoScore = String.valueOf(dataList.get(i).getScore());
            infoList.add(infoScore);
            String infoTime = dataList.get(i).getTime();
            infoList.add(infoTime);
        }
        Files.write(path, infoList, StandardOpenOption.WRITE);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rank_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        rankAdapter = new RankAdapter(dataList);
        recyclerView.setAdapter(rankAdapter);
    }

    private void buttonAction() {
        final EditText inputServer = new EditText(this);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入用户名").setIcon(R.drawable.ic_baseline_person_20).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (dialog, which) -> {
            String sign = inputServer.getText().toString();
            if(!sign.isEmpty())
            {
                name = sign;
                rankDao.doSearch(dateString).setName(name);
                dataList = rankDao.getAllInformation();
                recyclerView.setAdapter(new RankAdapter(dataList));
            }
            else
            {
                Toast.makeText(RankActivity.this,"用户名为空",Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

}