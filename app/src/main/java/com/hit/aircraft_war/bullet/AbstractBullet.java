package com.hit.aircraft_war.bullet;

import com.hit.aircraft_war.SingleActivity;
import com.hit.aircraft_war.basic.AbstractFlyingObject;

public class AbstractBullet extends AbstractFlyingObject {

    private int power = 10;

    public AbstractBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= SingleActivity.WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= SingleActivity.HEIGHT) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public int getPower() {
        return power;
    }
}
