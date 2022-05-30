package com.hit.aircraft_war.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.hit.aircraft_war.GameActivity;
import com.hit.aircraft_war.MainActivity;
import com.hit.aircraft_war.R;
import com.hit.aircraft_war.aircraft.BossEnemy;
import com.hit.aircraft_war.aircraft.EliteEnemy;
import com.hit.aircraft_war.aircraft.EnemyAircraft;
import com.hit.aircraft_war.aircraft.HeroAircraft;
import com.hit.aircraft_war.aircraft.MobEnemy;
import com.hit.aircraft_war.basic.AbstractFlyingObject;
import com.hit.aircraft_war.bullet.AbstractBullet;
import com.hit.aircraft_war.bullet.EnemyBullet;
import com.hit.aircraft_war.bullet.HeroBullet;
import com.hit.aircraft_war.factory.AbstractFactory;
import com.hit.aircraft_war.factory.BossFactory;
import com.hit.aircraft_war.factory.EnemyFactory;
import com.hit.aircraft_war.supply.AbstractSupply;
import com.hit.aircraft_war.supply.BombSupply;
import com.hit.aircraft_war.supply.FireSupply;
import com.hit.aircraft_war.supply.HpSupply;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class GameView extends SurfaceView implements
        SurfaceHolder.Callback,Runnable{

    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;
    private Paint mPaint;

    private int backGroundTop = 0;

    protected Bitmap backGround = null;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 20;

    private final HeroAircraft heroAircraft;
    private final List<EnemyAircraft> enemyAircrafts;
    private final List<AbstractBullet> heroBullets;
    private final List<AbstractBullet> enemyBullets;
    private final List<AbstractSupply> supply;

    protected int enemyMaxNumber;

    /**
     * boss生成条件
     * */
    protected int bossAppear;
    private int bossTime = 0;
    public static int bombScore = 0;
    protected int eliteRate;
    protected boolean needBoss;
    private int bossInterval;

    protected int upgradeBossHp = 0;


    private boolean gameOverFlag = false;
    public static boolean bossAppearFlag = false;

    private int screenHeight = MainActivity.HEIGHT;
    private int screenWidth = MainActivity.WIDTH;

    protected boolean changeBgm;
    private int score = 0;
    protected int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 300;
    private int cycleTime = 0;




    MediaPlayer bgmPlayer;
    MediaPlayer bossBgmPlayer;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();


    public GameView(Context context) {
        super(context);

        loading_img();

        bgmPlayer = MediaPlayer.create(context, R.raw.bgm);
        bossBgmPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
        mSoundPool = new SoundPool(4, AudioManager.STREAM_SYSTEM, 5);
        soundID.put(1, mSoundPool.load(context, R.raw.bullet_hit, 1));
        soundID.put(2, mSoundPool.load(context, R.raw.game_over, 1));
        soundID.put(3, mSoundPool.load(context, R.raw.bomb_explosion, 1));
        soundID.put(4, mSoundPool.load(context, R.raw.bullet, 1));
        soundID.put(5, mSoundPool.load(context, R.raw.get_supply, 1));


        mPaint = new Paint();//设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);

        heroAircraft = HeroAircraft.getInstance();
        heroController();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        supply = new LinkedList<>();

    }

    public static void stopMusic(MediaPlayer player) {
        if (player != null) {
            player.stop();
            player.reset();//重置
            player.release();//释放
        }
    }

    public void playBullet(){
        mSoundPool.play(soundID.get(4), 1, 1, 0,0,1);
    }

    public void playGameOver(){
        mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
    }

    public void playBulletHit() {
        mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
    }

    public void playBombExplosion() {
        mSoundPool.play(soundID.get(3), 1, 1, 0, 0, 1);
    }

    public void playGetSupply() {
        mSoundPool.play(soundID.get(5), 1, 1, 0, 0, 1);
    }


    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public final void action() {


        if(MainActivity.bgmFlag) {
            bgmPlayer.setLooping(true);
            if (bgmPlayer.isPlaying()) {
                bgmPlayer.start();
            }
        }

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定

        time += timeInterval;

        // 周期性执行（控制频率）
        if (timeCountAndNewCycleJudge()) {

            int ran = (int) (Math.random()*100+1);

            AbstractFactory enemyFactory = new EnemyFactory();
            AbstractFactory bossFactory = new BossFactory();

            //生成boss机
            bossInterval = score - bossTime*bossAppear;

            if (bossInterval >= bossAppear//每过400分生成一次
                    && !bossAppearFlag && needBoss){
                //达到阈值且没有boss则生成boss,无视最大敌机数量，只要满足条件就一定生成
                enemyAircrafts.add((EnemyAircraft) bossFactory.produceObject("BossEnemy",null));

                bossAppearFlag = true;
                bossTime ++;
                System.out.println("当前boss血量："+ (BossFactory.bossHp+BossFactory.addHp));
                BossFactory.addHp += upgradeBossHp;

                if (upgradeBossHp != 0){
                    System.out.println("下一个boss血量增加"+BossFactory.addHp+",即需要多射击"+(BossFactory.addHp/30)+"下");
                }

                bossInterval = 0;
            }

            if (bossInterval >= bossAppear && bossAppearFlag && needBoss){
                //到达下一个阈值时，boss还没打完，则bossTime也加一
                bossTime ++;
            }

            if (enemyAircrafts.size() < enemyMaxNumber) {
                //生成非boss机
                if(ran <= eliteRate){
                    //需要转一下类(AbstractFlyingObject -> EnemyAircraft)
                    enemyAircrafts.add((EnemyAircraft) enemyFactory.produceObject("MobEnemy",null));
                }
                else{
                    enemyAircrafts.add((EnemyAircraft) enemyFactory.produceObject("EliteEnemy",null));
                }
            }
            // 敌机射出子弹
            enemyShootAction();
        }
        // 英雄机射出子弹
        if (heroShootRate()){
            heroBullets.addAll(heroAircraft.shoot());
            if(MainActivity.bgmFlag) {
                playBullet();
            }
        }

        // 子弹移动
        bulletsMoveAction();

        // 道具移动
        supplyMoveAction();

        // 飞机移动
        aircraftsMoveAction();

        // 撞击检测
        crashCheckAction();

        // 后处理
        postProcessAction();

        //难度变化
        difficultUpgrade();

        //每个时刻重绘
        draw();

        // 游戏结束检查
        if (heroAircraft.getHp() <= 0) {

            if(MainActivity.bgmFlag) {
                stopMusic(bgmPlayer);
                stopMusic(bossBgmPlayer);
                playGameOver();
            }
            // 游戏结束
            gameOverFlag = true;
            System.out.println("Game Over!");
        }

    }

    //***********************
    //      Action 各部分
    //***********************
    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void enemyShootAction() {
        // 敌机射击
        for (int i=0 ; i<enemyAircrafts.size() ; i++) {
            //敌机被摧毁后不再射击
            if (!enemyAircrafts.get(i).notValid()){
                //所有敌机都有shoot函数，但是Mob的shoot为空，无操作
                enemyBullets.addAll(enemyAircrafts.get(i).shoot());
            }
        }
        if(MainActivity.bgmFlag) {
            playBullet();
        }

    }

    private void bulletsMoveAction() {
        for (int i=0 ; i<heroBullets.size() ; i++) {
            heroBullets.get(i).forward();
        }
        for (int i=0 ; i<enemyBullets.size() ; i++) {
            enemyBullets.get(i).forward();
        }
    }

    private void supplyMoveAction(){
        for (int i=0 ; i<supply.size() ; i++){
            supply.get(i).forward();
        }
    }

    private void aircraftsMoveAction() {
        for (int i=0 ; i<enemyAircrafts.size() ; i++) {
            enemyAircrafts.get(i).forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {

        // 敌机子弹攻击英雄
        for (int i=0 ; i<enemyBullets.size() ; i++) {
            if (enemyBullets.get(i).notValid()) {
                //消失的子弹不检测
                continue;
            }
            //因为敌机子弹只会撞击英雄机，且英雄机只有一个，所以无需过多的步骤
            if (heroAircraft.crash(enemyBullets.get(i))) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(enemyBullets.get(i).getPower());

                if(MainActivity.bgmFlag) {
                    playBulletHit();
                }
                enemyBullets.get(i).vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (int i=0 ; i<heroBullets.size() ; i++) {
            if (heroBullets.get(i).notValid()) {
                continue;
            }
            for (int j=0 ; j<enemyAircrafts.size() ; j++) {
                if (enemyAircrafts.get(j).notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircrafts.get(j).crash(heroBullets.get(i))) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值

                    if(MainActivity.bgmFlag) {
                        playBulletHit();
                    }

                    enemyAircrafts.get(j).decreaseHp(heroBullets.get(i).getPower());
                    heroBullets.get(i).vanish();


                    if (enemyAircrafts.get(j).notValid()) {
                        //判断是否为精英敌机
                        if(enemyAircrafts.get(j) instanceof EliteEnemy){
                            // 获得分数，产生道具补给
                            // 限定只有80%的概率生成道具
                            score += 30;
                            int ran = (int)(Math.random()*100 +1);
                               if (ran <= 90){
                                   supply.add(enemyAircrafts.get(j).bonus());
                               }
                        }else if (enemyAircrafts.get(j) instanceof BossEnemy){
                            // 先标记boss消失
                            // 再生成道具
                            bossAppearFlag = false;
                            score += 90;

                            if(MainActivity.bgmFlag) {
                                stopMusic(bossBgmPlayer);
                                bgmPlayer.setLooping(true);
                                if (bgmPlayer.isPlaying()){
                                    bgmPlayer.start();}

                            }

                            int propNum = ((BossEnemy) enemyAircrafts.get(j)).getPropNum();
                            for (int k=0; k<propNum; k++){
                                //因为boss会生成两个道具，所以位置应该调整，并且boss没有y方向速度，所以要给道具添加上
                                AbstractSupply bossSupply = enemyAircrafts.get(j).bonus();
                                bossSupply.setSpeedY(15);
                                bossSupply.setLocation(enemyAircrafts.get(j).getLocationX() +(k*2 - propNum + 1)*160,enemyAircrafts.get(j).getLocationY());
                                supply.add(bossSupply);
                            }
                        } else{
                            score += 10;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircrafts.get(j).crash(heroAircraft) || heroAircraft.crash(enemyAircrafts.get(j))) {
                    enemyAircrafts.get(j).vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }


        // 我方获得道具，道具生效
        for (int i=0 ; i<supply.size() ; i++){
            if (supply.get(i).notValid()){
                //道具不存在则跳过该道具
                continue;
            }
            //英雄机碰到道具
            if (heroAircraft.crash(supply.get(i))){
                if (supply.get(i) instanceof HpSupply){


                    //加血道具
                    if(MainActivity.bgmFlag) {
                        playGetSupply();
                    }
                    ((HpSupply) supply.get(i)).executeHp(heroAircraft);

                }
                else if (supply.get(i) instanceof FireSupply){
                    //火力道具生效15s
                        if(MainActivity.bgmFlag) {
                            playGetSupply();}

                    ((FireSupply) supply.get(i)).executeFire(heroAircraft);

                }
                else if (supply.get(i) instanceof BombSupply){
                    //炸弹道具
                    if(MainActivity.bgmFlag) {
                            playBombExplosion();}

                    //添加订阅者
                    for (int j=0 ; j<enemyBullets.size() ; j++){
                        ((BombSupply) supply.get(i)).addObject(enemyBullets.get(j));
                    }
                    assert enemyAircrafts != null;
                    for (int j=0 ; j<enemyAircrafts.size() ; j++){
                        ((BombSupply) supply.get(i)).addObject(enemyAircrafts.get(j));
                    }
                    ((BombSupply) supply.get(i)).executeBomb(this);

                }
                else{
                    //显示是否出现未知道具，便于更改
                    System.out.println("ERROR!");
                }
                //道具生效后消失
                supply.get(i).vanish();
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效道具
     * 4. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supply.removeIf(AbstractFlyingObject::notValid);
    }

    //***********************
    //      加载图片
    //***********************

    public void loading_img(){
        ImageManager.BACKGROUND_IMAGE1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ImageManager.BACKGROUND_IMAGE2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
        ImageManager.BACKGROUND_IMAGE3 = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);

        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.HP_SUPPLY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.FIRE_SUPPLY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);
        ImageManager.BOMB_SUPPLY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);

        //装填到HashMap
        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), ImageManager.HERO_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), ImageManager.MOB_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ImageManager.ELITE_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), ImageManager.BOSS_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), ImageManager.HERO_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ImageManager.ENEMY_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HpSupply.class.getName(), ImageManager.HP_SUPPLY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(FireSupply.class.getName(), ImageManager.FIRE_SUPPLY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BombSupply.class.getName(), ImageManager.BOMB_SUPPLY_IMAGE);

    }

    //***********************
    //      英雄机控制
    //***********************

    public void heroController() {
        this.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                double latestX = motionEvent.getX();
                double latestY = motionEvent.getY();

                if ( latestX<0 || latestX>MainActivity.WIDTH || latestY<0 || latestY>MainActivity.HEIGHT){
                    //防止出界
                    return false;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    heroAircraft.setLocation(latestX,latestY);
                }
                return true;
            }

        });
    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     *draw
     */
    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if (mSurfaceHolder == null || canvas == null){
            return;
        }

        mPaint.setAntiAlias(true);
        // 绘制背景,图片滚动
        canvas.drawBitmap(backGround, 0, this.backGroundTop - screenHeight, mPaint);
        canvas.drawBitmap(backGround, 0, this.backGroundTop, mPaint);
        this.backGroundTop += 2;
        if (this.backGroundTop == screenHeight) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，再绘制道具，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(canvas, enemyBullets);
        paintImageWithPositionRevised(canvas, supply);
        paintImageWithPositionRevised(canvas, heroBullets);

        paintImageWithPositionRevised(canvas, enemyAircrafts);

        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, mPaint);

        //绘制得分和生命值
        paintScoreAndLife(canvas);

        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void paintImageWithPositionRevised(Canvas canvas, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }else {
            for (int i = 0; i< objects.size(); i++) {
                Bitmap image = objects.get(i).getImage();
                assert image != null : objects.getClass().getName() + " has no image! ";
                canvas.drawBitmap(image, objects.get(i).getLocationX() - image.getWidth() / 2,
                        objects.get(i).getLocationY() - image.getHeight() / 2, mPaint);
            }
        }
    }

    private void paintScoreAndLife(Canvas canvas) {
        int x = 20;
        int y = 60;

        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setColor(Color.RED);
        mPaint.setTypeface(font);
        mPaint.setTextSize(80);
        canvas.drawText("SCORE:" + this.score, x, y, mPaint);
        y = y + 80;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
    }


    public abstract void difficultUpgrade();//提升难度

    public abstract boolean heroShootRate();//控制英雄机设计频率

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }



    @Override
    public void run(){
        while (!gameOverFlag){
            synchronized (mSurfaceHolder){
                action();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Message msg = Message.obtain();
        msg.what = 0;
        msg.obj = score;

        GameActivity.handler.sendMessage(msg);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        screenHeight = height;
        screenWidth = width;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        gameOverFlag = true;
    }
}
