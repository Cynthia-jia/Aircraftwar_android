package com.hit.aircraft_war.supply;

import com.hit.aircraft_war.aircraft.HeroAircraft;

public class FireSupply extends AbstractSupply{

    public FireSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void executeFire(HeroAircraft heroAircraft){
        System.out.println("火力道具生效");
        Runnable fire = () -> {
            this.executeIncreaseFire(heroAircraft);

            try {
                //15秒火力加强
                Thread.sleep(15*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            this.executeDecreaseFire(heroAircraft);
        };

        //线程启动
        new Thread(fire,"fire").start();
    }
    public void executeIncreaseFire(HeroAircraft heroAircraft){
        heroAircraft.increaseShootNum(1);
        heroAircraft.changeScatterMode();
    }

    public void executeDecreaseFire(HeroAircraft heroAircraft){
        heroAircraft.decreaseShootNum(1);
        if (heroAircraft.getShootNum() == 1){
            heroAircraft.changeStraightMode();
        }
    }

}
