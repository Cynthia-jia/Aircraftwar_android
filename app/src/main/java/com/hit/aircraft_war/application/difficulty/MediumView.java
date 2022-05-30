package com.hit.aircraft_war.application.difficulty;

import android.content.Context;

import com.hit.aircraft_war.aircraft.EliteEnemy;
import com.hit.aircraft_war.application.GameView;
import com.hit.aircraft_war.application.ImageManager;
import com.hit.aircraft_war.factory.BossFactory;
import com.hit.aircraft_war.factory.EnemyFactory;
import com.hit.aircraft_war.strategy.BossShoot;

public class MediumView extends GameView {

    public MediumView(Context context) {
        super(context);
        this.backGround = ImageManager.BACKGROUND_IMAGE2;
        this.needBoss = true;
        this.eliteRate = 80;
        this.enemyMaxNumber = 6;
        this.upgradeBossHp = 0;
        this.bossAppear = 700;
        this.changeBgm = false;
        EliteEnemy.isHard = false;
        BossFactory.isHpUpgrade = false;
        BossShoot.easyBossShoot = true;
    }

    @Override
    public void difficultUpgrade() {
        if (this.time == 30000){
            EnemyFactory.upgradeMobSpeed = 2;
            System.out.println("难度提升，普通机速度加快");
        }
        if (this.time == 45000){
            EliteEnemy.isHard = true;
            System.out.println("难度提升，精英机开始散射");
        }
        if (this.time % 22500 == 0 && this.time>0){
            if (eliteRate > 75){
                this.eliteRate -= 1;
                System.out.println("难度提升，当前精英机生成概率："+(100-eliteRate)+"%");
            }
            if (EnemyFactory.upgradeEliteHp < 60){
                EnemyFactory.upgradeEliteHp += 15;
                System.out.println("难度提升，当前精英机血量为："+(90+EnemyFactory.upgradeEliteHp));
            }
        }
    }

    @Override
    public boolean heroShootRate() {
        return this.time % 260 == 0;
    }
}
