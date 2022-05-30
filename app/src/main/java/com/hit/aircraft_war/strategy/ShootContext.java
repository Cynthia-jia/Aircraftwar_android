package com.hit.aircraft_war.strategy;

import com.hit.aircraft_war.aircraft.AbstractAircraft;
import com.hit.aircraft_war.bullet.AbstractBullet;

import java.util.List;

public class ShootContext {
    private ShootStrategy shootStrategy;

    public ShootContext(ShootStrategy shootStrategy){
        this.shootStrategy = shootStrategy;
    }

    public void setShootStrategy(ShootStrategy shootStrategy){
        this.shootStrategy = shootStrategy;
    }

    public List<AbstractBullet> executeShootStrategy(AbstractAircraft aircraft, int direction, int shootNum, int power){
        return this.shootStrategy.ballistic(aircraft,direction,shootNum,power);
    }
}
