package com.hit.aircraft_war.aircraft;

import com.hit.aircraft_war.MainActivity;
import com.hit.aircraft_war.application.GameView;
import com.hit.aircraft_war.bullet.AbstractBullet;
import com.hit.aircraft_war.factory.AbstractFactory;
import com.hit.aircraft_war.factory.SupplyFactory;
import com.hit.aircraft_war.strategy.ShootContext;
import com.hit.aircraft_war.strategy.ScatteredShoot;
import com.hit.aircraft_war.strategy.StraightShoot;
import com.hit.aircraft_war.supply.AbstractSupply;

import java.util.List;

public class EliteEnemy extends EnemyAircraft{

    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 10;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;

    public static boolean isHard = false;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<AbstractBullet> shoot() {
        //策略模式
        ShootContext shootContext = new ShootContext(new StraightShoot());
        if (!isHard){
            return shootContext.executeShootStrategy(this,direction,shootNum,power);
        }else {
            shootContext.setShootStrategy(new ScatteredShoot());
            return shootContext.executeShootStrategy(this,direction,2*shootNum,power);
        }

    }

    @Override
    public AbstractSupply bonus() {
        //生成道具

        AbstractSupply abstractSupply = null;
        //利用工厂模式生成
        AbstractFactory supplyFactory = new SupplyFactory();

        if (this.notValid()){
            int ran = (int)(Math.random()*100+1);
            /**生成道具 ，在生成道具的情况下
             * 50%概率生成加血道具
             * 30%概率生成火力道具
             * 20%概率生成炸弹道具
             * */
            if (ran <= 50){
                //需要转一下类(AbstractFlyingObject -> AbstractSupply)
                abstractSupply = (AbstractSupply) supplyFactory.produceObject("HpSupply",this);
            }
            else if (ran <= 80){
                abstractSupply = (AbstractSupply) supplyFactory.produceObject("FireSupply",this);
            }
            else {
                abstractSupply = (AbstractSupply) supplyFactory.produceObject("BombSupply",this);
            }

        }
        return abstractSupply;
    }

    @Override
    public void upgrade(){
        this.vanish();
        GameView.bombScore += 30;
    }
}
