package com.hit.aircraft_war.factory;

import com.hit.aircraft_war.MainActivity;
import com.hit.aircraft_war.aircraft.EliteEnemy;
import com.hit.aircraft_war.aircraft.EnemyAircraft;
import com.hit.aircraft_war.aircraft.MobEnemy;
import com.hit.aircraft_war.application.ImageManager;
import com.hit.aircraft_war.basic.AbstractFlyingObject;

public class EnemyFactory extends AbstractFactory{

    public static int upgradeEliteHp = 0;

    public static int upgradeMobSpeed = 0;
    @Override
    AbstractFlyingObject createObject(String type, EnemyAircraft enemy) {
        AbstractFlyingObject enemyAircraft = null;

        switch (type) {
            case "MobEnemy":
                enemyAircraft = new MobEnemy(
                        (int) (Math.random() * (MainActivity.WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                        (int) (Math.random() * MainActivity.HEIGHT * 0.2),
                        0,
                        36+upgradeMobSpeed,
                        30);
                break;
            case "EliteEnemy":
                int ran = (int) (Math.random()*20+1);

                if (ran <= 10){
                    //不会左右移动
                    enemyAircraft = new EliteEnemy(
                            (int) (Math.random() * (MainActivity.WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                            (int) (Math.random() * MainActivity.HEIGHT * 0.2),
                            0,
                            18,
                            90+upgradeEliteHp);
                }else {
                    //会左右移动
                    enemyAircraft = new EliteEnemy(
                            (int) (Math.random() * (MainActivity.WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                            (int) (Math.random() * MainActivity.HEIGHT * 0.2),
                            15,
                            15,
                            90+upgradeEliteHp);
                }
                break;
            default:
                break;
        }

        return enemyAircraft;
    }
}
