package com.hit.aircraft_war.application.difficulty;

import android.content.Context;

import com.hit.aircraft_war.aircraft.EliteEnemy;
import com.hit.aircraft_war.application.GameView;
import com.hit.aircraft_war.application.ImageManager;
import com.hit.aircraft_war.factory.BossFactory;
import com.hit.aircraft_war.factory.EnemyFactory;
import com.hit.aircraft_war.strategy.BossShoot;

public class HardView extends GameView {

    public HardView(Context context) {
        super(context);
        this.backGround = ImageManager.BACKGROUND_IMAGE3;
        this.needBoss = true;
        this.eliteRate = 75;
        this.enemyMaxNumber =7;
        this.upgradeBossHp = 150;
        this.bossAppear = 500;
        this.changeBgm = true;
        EliteEnemy.isHard = true;
        BossFactory.isHpUpgrade = true;
        BossShoot.easyBossShoot = false;
    }

    @Override
    public void difficultUpgrade() {
        if (this.time % 45000 == 0 && this.time>0){
            if (this.eliteRate > 70){
                this.eliteRate -= 1;
                System.out.println("难度提升，当前精英机生成概率："+(100-eliteRate)+"%");
            }
            if (EnemyFactory.upgradeEliteHp < 90){
                EnemyFactory.upgradeEliteHp += 30;
                System.out.println("难度提升，当前精英机血量为："+(90+EnemyFactory.upgradeEliteHp));
            }
            if (EnemyFactory.upgradeMobSpeed < 4){
                EnemyFactory.upgradeMobSpeed += 1;
                System.out.println("难度提升，当前普通机速度为："+(9+EnemyFactory.upgradeMobSpeed));
            }
        }
    }

    @Override
    public boolean heroShootRate() {
        return this.time % 240 == 0;
    }
}
