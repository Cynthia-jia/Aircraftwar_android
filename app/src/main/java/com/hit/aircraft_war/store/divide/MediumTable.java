package com.hit.aircraft_war.store.divide;

import org.litepal.crud.LitePalSupport;

public class MediumTable extends LitePalSupport implements Table{
    private String name;
    private int score;
    private String time;

    public MediumTable (String name, int score, String time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public MediumTable () {}

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
