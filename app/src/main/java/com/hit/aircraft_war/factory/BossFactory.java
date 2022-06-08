package com.hit.aircraft_war.factory;

import com.hit.aircraft_war.SingleActivity;
import com.hit.aircraft_war.aircraft.BossEnemy;
import com.hit.aircraft_war.aircraft.EnemyAircraft;
import com.hit.aircraft_war.application.ImageManager;
import com.hit.aircraft_war.basic.AbstractFlyingObject;

public class BossFactory extends AbstractFactory{
    public static int addHp = 0;
    public static boolean isHpUpgrade = false;
    public static int bossHp = 0;
    @Override
    AbstractFlyingObject createObject(String type, EnemyAircraft enemy) {
        AbstractFlyingObject boss = null;

        bossHp = isHpUpgrade?1200:900;

        if (type.equals("BossEnemy")){
            boss = new BossEnemy(
                    (int) (Math.random() * (SingleActivity.WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                    (int) (Math.random() * SingleActivity.HEIGHT * 0.15),
                    15,
                    0,
                    bossHp+addHp);
        }

        return boss;
    }

}
