package com.hit.aircraft_war.strategy;

import com.hit.aircraft_war.aircraft.AbstractAircraft;
import com.hit.aircraft_war.bullet.AbstractBullet;

import java.util.List;

public interface ShootStrategy {
    List<AbstractBullet> ballistic(AbstractAircraft aircraft, int direction, int shootNum, int power);
}
