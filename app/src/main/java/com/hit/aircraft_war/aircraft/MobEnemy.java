package com.hit.aircraft_war.aircraft;

import com.hit.aircraft_war.MainActivity;
import com.hit.aircraft_war.application.GameView;
import com.hit.aircraft_war.bullet.AbstractBullet;
import com.hit.aircraft_war.supply.AbstractSupply;

import java.util.LinkedList;
import java.util.List;

public class MobEnemy extends EnemyAircraft{

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<AbstractBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public AbstractSupply bonus() { return null; }

    @Override
    public void upgrade(){
        this.vanish();
        GameView.bombScore += 10;
    }
}
