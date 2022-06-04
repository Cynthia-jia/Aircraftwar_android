package com.hit.aircraft_war.store.dao;

public class RankMember {
    private int rank;
    private String name;
    private int score;
    private String time;

    public RankMember(int rank, String name, int score, String time) {
        this.rank = rank;
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {this.rank = rank;}

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
