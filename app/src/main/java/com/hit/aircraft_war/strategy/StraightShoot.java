package com.hit.aircraft_war.strategy;

import com.hit.aircraft_war.aircraft.AbstractAircraft;
import com.hit.aircraft_war.aircraft.HeroAircraft;
import com.hit.aircraft_war.bullet.AbstractBullet;
import com.hit.aircraft_war.bullet.EnemyBullet;
import com.hit.aircraft_war.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class StraightShoot implements ShootStrategy{
    //直射模式
    @Override
    public List<AbstractBullet> ballistic(AbstractAircraft aircraft, int direction, int shootNum, int power) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction*36;
        AbstractBullet abstractBullet;
        for (int i=0; i<shootNum ;i++){
            //多子弹轨道偏移
            if(aircraft instanceof HeroAircraft){
                abstractBullet = new HeroBullet(x +(i*2 - shootNum + 1)*8,y,speedX,speedY,power);
            }else{
                abstractBullet = new EnemyBullet(x +(i*2 - shootNum + 1)*10,y,speedX,speedY,power);
            }

            res.add(abstractBullet);
        }

        return res;
    }
}
