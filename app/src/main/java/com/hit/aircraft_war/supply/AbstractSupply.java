package com.hit.aircraft_war.supply;

import com.hit.aircraft_war.MainActivity;
import com.hit.aircraft_war.basic.AbstractFlyingObject;

public class AbstractSupply extends AbstractFlyingObject {

    public AbstractSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward(){
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= MainActivity.WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= MainActivity.HEIGHT) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

}
