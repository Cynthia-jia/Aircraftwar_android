package com.hit.aircraft_war.factory;

import com.hit.aircraft_war.aircraft.EnemyAircraft;
import com.hit.aircraft_war.basic.AbstractFlyingObject;

public abstract class AbstractFactory {
    public AbstractFlyingObject produceObject(String type, EnemyAircraft enemy){
        AbstractFlyingObject flyingObject;
        //后一项enemy为SupplyFactory所需，EnemyFactory使用时只传null即可
        flyingObject = createObject(type,enemy);

        return flyingObject;
    }

    abstract AbstractFlyingObject createObject(String type, EnemyAircraft enemy);
}
