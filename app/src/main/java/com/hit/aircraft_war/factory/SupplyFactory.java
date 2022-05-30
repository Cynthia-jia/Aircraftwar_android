package com.hit.aircraft_war.factory;

import com.hit.aircraft_war.aircraft.EnemyAircraft;
import com.hit.aircraft_war.basic.AbstractFlyingObject;
import com.hit.aircraft_war.supply.BombSupply;
import com.hit.aircraft_war.supply.FireSupply;
import com.hit.aircraft_war.supply.HpSupply;

public class SupplyFactory extends AbstractFactory{
    @Override
    AbstractFlyingObject createObject(String type, EnemyAircraft enemy) {
        AbstractFlyingObject supply = null;

        switch (type) {
            case "HpSupply":
                supply = new HpSupply(
                        enemy.getLocationX(),
                        enemy.getLocationY(),
                        0,
                        enemy.getSpeedY()
                );
                break;
            case "FireSupply":
                supply = new FireSupply(
                        enemy.getLocationX(),
                        enemy.getLocationY(),
                        0,
                        enemy.getSpeedY()
                );
                break;
            case "BombSupply":
                supply = new BombSupply(
                        enemy.getLocationX(),
                        enemy.getLocationY(),
                        0,
                        enemy.getSpeedY()
                );
                break;
            default:
                break;
        }

        return supply;
    }
}
