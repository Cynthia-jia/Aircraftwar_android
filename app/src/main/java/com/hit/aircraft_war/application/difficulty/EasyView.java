package com.hit.aircraft_war.application.difficulty;

import android.content.Context;

import com.hit.aircraft_war.aircraft.EliteEnemy;
import com.hit.aircraft_war.application.GameView;
import com.hit.aircraft_war.application.ImageManager;

public class EasyView extends GameView {

    public EasyView(Context context) {
        super(context);
        this.backGround = ImageManager.BACKGROUND_IMAGE1;
        this.needBoss = false;
        this.eliteRate = 85;
        this.enemyMaxNumber = 5;
        this.upgradeBossHp = 0;
        this.bossAppear = Integer.MAX_VALUE;
        this.changeBgm = false;
        EliteEnemy.isHard = false;
    }

    @Override
    public void difficultUpgrade() {

    }

    @Override
    public boolean heroShootRate() {
        return this.time % 300 == 0;
    }
}
