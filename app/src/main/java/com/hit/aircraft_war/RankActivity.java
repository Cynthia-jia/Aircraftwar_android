package com.hit.aircraft_war;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.aircraft_war.store.RankAdapter;
import com.hit.aircraft_war.store.dao.RankDao;
import com.hit.aircraft_war.store.dao.RankDaoImpl;
import com.hit.aircraft_war.store.dao.RankMember;
import com.hit.aircraft_war.store.divide.EasyTable;
import com.hit.aircraft_war.store.divide.HardTable;
import com.hit.aircraft_war.store.divide.MediumTable;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    }

    @SuppressLint("SdCardPath")
    private void initData() throws IOException {
        rankDao = new RankDaoImpl();
        //获取时间
        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateString = dateFormat.format(currentDate);
        //获取用户名
        Intent lastIntent = getIntent();
        if (ProfileActivity.isChanged) {
            name = ProfileActivity.newName;
        }else {
            name = lastIntent.getStringExtra("userName");
        }

        if (SingleActivity.difficultChoice == 0){
            EasyTable easyTable = new EasyTable(name, newSCore, dateString);
            easyTable.save();

            List<EasyTable> easyTables = LitePal.order("score desc").find(EasyTable.class);;
            for (int i=0; i<easyTables.size(); i++) {
                String name = easyTables.get(i).getName();
                int score = easyTables.get(i).getScore();
                String time = easyTables.get(i).getTime();
                rankDao.doAdd(new RankMember(i+1, name, score, time));
            }

        }else if (SingleActivity.difficultChoice == 1){
            MediumTable mediumTable = new MediumTable(name, newSCore, dateString);
            mediumTable.save();

            List<MediumTable> mediumTables = LitePal.order("score desc").find(MediumTable.class);;
            for (int i=0; i<mediumTables.size(); i++) {
                String name = mediumTables.get(i).getName();
                int score = mediumTables.get(i).getScore();
                String time = mediumTables.get(i).getTime();
                rankDao.doAdd(new RankMember(i+1, name, score, time));
            }
        }else {
            HardTable hardTable = new HardTable(name, newSCore, dateString);
            hardTable.save();

            List<HardTable> hardTables = LitePal.order("score desc").find(HardTable.class);;
            for (int i=0; i<hardTables.size(); i++) {
                String name = hardTables.get(i).getName();
                int score = hardTables.get(i).getScore();
                String time = hardTables.get(i).getTime();
                rankDao.doAdd(new RankMember(i+1, name, score, time));
            }
        }
        //生成数据源
        dataList = rankDao.getAllInformation();

    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rank_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        rankAdapter = new RankAdapter(dataList);
        recyclerView.setAdapter(rankAdapter);
    }

}