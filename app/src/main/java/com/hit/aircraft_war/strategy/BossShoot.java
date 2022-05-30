package com.hit.aircraft_war.strategy;

import com.hit.aircraft_war.aircraft.AbstractAircraft;
import com.hit.aircraft_war.bullet.AbstractBullet;
import com.hit.aircraft_war.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class BossShoot implements ShootStrategy{
    public static boolean easyBossShoot = true;
    @Override
    public List<AbstractBullet> ballistic(AbstractAircraft aircraft, int direction, int shootNum, int power) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*3;
        int speedY = aircraft.getSpeedY() + direction*36;
        AbstractBullet abstractBullet;
        int ran = (int) (Math.random()*100+1);
        if (easyBossShoot){
            if (ran <= 60){
                tripleShoot(shootNum, power, res, x, y, speedY);
            }else {
                denseShoot(shootNum, power, res, x, y, speedY);
            }
        }else {
            if (ran <= 30){
                tripleShoot(shootNum, power, res, x, y, speedY);
            } else if (ran <= 70) {
                denseShoot(shootNum, power, res, x, y, speedY);
                for (int i=0; i < shootNum; i++){
                    abstractBullet = new EnemyBullet(x-360 + (i * 2 - shootNum + 1),y,0,speedY,power);
                    res.add(abstractBullet);
                }
                for (int i=0; i < shootNum; i++){
                    abstractBullet = new EnemyBullet(x+360 + (i * 2 - shootNum + 1),y,0,speedY,power);
                    res.add(abstractBullet);
                }
            } else {
                for (int i=0; i < shootNum; i++){
                    abstractBullet = new EnemyBullet(x ,y,(i*2 - shootNum + 1)*9,speedY,power);
                    res.add(abstractBullet);
                }
                for (int i=0; i < shootNum; i++){
                    abstractBullet = new EnemyBullet(x ,y-30,(i*2 - shootNum + 1)*6,speedY,power);
                    res.add(abstractBullet);
                }
                for (int i=0; i < shootNum; i++){
                    abstractBullet = new EnemyBullet(x ,y-60,(i*2 - shootNum + 1)*3,speedY,power);
                    res.add(abstractBullet);
                }
            }
        }


        return res;
    }

    private void denseShoot(int shootNum, int power, List<AbstractBullet> res, int x, int y, int speedY) {
        AbstractBullet abstractBullet;
        for (int i = 0; i < shootNum; i++){
            abstractBullet = new EnemyBullet(x + (i * 2 - shootNum + 1),y,0,speedY,power);
            res.add(abstractBullet);
        }
        for (int i=0; i < shootNum; i++){
            abstractBullet = new EnemyBullet(x-180 + (i * 2 - shootNum + 1),y,0,speedY,power);
            res.add(abstractBullet);
        }
        for (int i=0; i < shootNum; i++){
            abstractBullet = new EnemyBullet(x+180 + (i * 2 - shootNum + 1),y,0,speedY,power);
            res.add(abstractBullet);
        }
    }

    private void tripleShoot(int shootNum, int power, List<AbstractBullet> res, int x, int y, int speedY) {
        AbstractBullet abstractBullet;
        for (int i = 0; i<shootNum/3; i++){
            //多子弹轨道偏移,且有直射和散射效果
            abstractBullet = new EnemyBullet(x +(i*2 - shootNum + 1)*8,y,(i*2 - shootNum + 1)*2,speedY,power);
            res.add(abstractBullet);
        }
        for (int i=shootNum/3; i<4*shootNum/5; i++){
            abstractBullet = new EnemyBullet(x +(i*2 - shootNum + 1)*60,y,0,speedY,power);
            res.add(abstractBullet);
        }
        for (int i=4*shootNum/5; i<shootNum; i++){
            abstractBullet = new EnemyBullet(x +(i*2 - shootNum + 1)*8,y,(i*2 - shootNum + 1)*2,speedY,power);
            res.add(abstractBullet);
        }
    }
}
