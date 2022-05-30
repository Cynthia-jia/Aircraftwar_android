package com.hit.aircraft_war.bullet;

public class EnemyBullet extends AbstractBullet{

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void upgrade() {
        this.vanish();
    }
}
