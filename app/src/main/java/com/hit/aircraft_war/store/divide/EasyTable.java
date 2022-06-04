package com.hit.aircraft_war.store.divide;

import org.litepal.crud.LitePalSupport;

public class EasyTable extends LitePalSupport implements Table{
    private String name;
    private int score;
    private String time;

    public EasyTable (String name, int score, String time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public EasyTable () {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
