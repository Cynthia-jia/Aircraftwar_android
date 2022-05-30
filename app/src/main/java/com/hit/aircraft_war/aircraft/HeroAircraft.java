package com.hit.aircraft_war.aircraft;

import com.hit.aircraft_war.MainActivity;
import com.hit.aircraft_war.application.ImageManager;
import com.hit.aircraft_war.bullet.AbstractBullet;
import com.hit.aircraft_war.strategy.ShootContext;
import com.hit.aircraft_war.strategy.ScatteredShoot;
import com.hit.aircraft_war.strategy.StraightShoot;

import java.util.List;

public class HeroAircraft extends AbstractAircraft{
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;//todo

    private int shootMax = 6;//最大射击数量

    private int shootMin = 1;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = -1;

    /**
     * 射击模式 (直射：false，散射：true)
     */
    private boolean shootMode = false;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */


    //单例，这里采用饿汉式
    private static HeroAircraft heroAircraft = new HeroAircraft(
            MainActivity.WIDTH / 2,
            MainActivity.HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
            0, 0, 1000);

    //封闭构造方法
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    //单例获取方法
    public static HeroAircraft getInstance(){
        return heroAircraft;
    }

    public int getShootNum() {
        return shootNum;
    }

    public void increaseShootNum(int increase){
        if (shootNum < shootMax){
            shootNum = shootNum + increase;
        }
    }

    public void decreaseShootNum(int decrease){
        if (shootNum > shootMin){
            shootNum = shootNum - decrease;
        }
    }

    public void changeScatterMode(){
        shootMode = true;
    }
    public void changeStraightMode() {
        shootMode = false;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<AbstractBullet> shoot() {
        ShootContext shootContext = new ShootContext(new StraightShoot());
        if (shootMode){
            shootContext.setShootStrategy(new ScatteredShoot());
        }

        return shootContext.executeShootStrategy(this,direction,shootNum,power);
    }
}
