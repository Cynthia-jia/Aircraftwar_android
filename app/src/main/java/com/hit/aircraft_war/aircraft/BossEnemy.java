package com.hit.aircraft_war.aircraft;

import com.hit.aircraft_war.application.GameView;
import com.hit.aircraft_war.bullet.AbstractBullet;
import com.hit.aircraft_war.factory.AbstractFactory;
import com.hit.aircraft_war.factory.SupplyFactory;
import com.hit.aircraft_war.strategy.BossShoot;
import com.hit.aircraft_war.strategy.ShootContext;
import com.hit.aircraft_war.supply.AbstractSupply;

import java.util.List;

public class BossEnemy extends EnemyAircraft{
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 11;

    /**
     * 道具生成数量
     */
    private int propNum = 2;

    /**
     * 子弹伤害
     */
    private int power = 10;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;

    private int bombPower = 300;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public int getPropNum(){
        return propNum;
    }

    @Override
    public List<AbstractBullet> shoot() {
        ShootContext shootContext = new ShootContext(new BossShoot());
        return shootContext.executeShootStrategy(this,direction,shootNum,power);
    }

    @Override
    public AbstractSupply bonus() {
        //生成道具

        AbstractSupply abstractSupply = null;
        //利用工厂模式生成
        AbstractFactory supplyFactory = new SupplyFactory();

        if (this.notValid()){
            int ran = (int)(Math.random()*100+1);
            /**定义boss战过后奖励的道具只有加血道具和火力道具
             * 50%概率生成加血道具
             * 50%概率生成火力道具
             * */
            if (ran <= 50) {
                //需要转一下类(AbstractFlyingObject -> AbstractSupply)
                abstractSupply = (AbstractSupply) supplyFactory.produceObject("HpSupply", this);
            }
            else {
                abstractSupply = (AbstractSupply) supplyFactory.produceObject("FireSupply",this);
            }

        }
        return abstractSupply;
    }

    @Override
    public void upgrade(){
        this.decreaseHp(bombPower);
        if (this.notValid()){
            GameView.bossAppearFlag = false;
            GameView.bombScore += 90;
        }
    }
}
