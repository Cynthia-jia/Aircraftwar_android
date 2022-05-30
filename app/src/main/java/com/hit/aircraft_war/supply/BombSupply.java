package com.hit.aircraft_war.supply;

import com.hit.aircraft_war.application.GameView;
import com.hit.aircraft_war.basic.AbstractFlyingObject;

import java.util.LinkedList;
import java.util.List;

public class BombSupply extends AbstractSupply{

    private List<AbstractFlyingObject> flyingObjectList = new LinkedList<>();

    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void addObject(AbstractFlyingObject flyingObject){
        flyingObjectList.add(flyingObject);
    }

    //通知所有观察者
    public void callAll(){
        for (AbstractFlyingObject flyingObject:flyingObjectList){
            flyingObject.upgrade();
        }
    }

    //炸弹生效
    public void executeBomb(GameView gameView){
        System.out.println("炸弹道具生效");
        callAll();
        gameView.setScore(gameView.getScore()+ GameView.bombScore);
        GameView.bombScore = 0;
    }
}
