package com.csapat.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Vector;

public class Enemy extends Sprite {
    protected Directions moveDirection;
    protected Directions attackDirection;
    protected float speed;
    private Directions lastMove;
    protected int healthPoints;
    protected int attackRange;
    protected int damage = 10;
    protected String name;
    protected int velx;
    protected int vely;
    protected final int visionRange;
    protected final int levelDepth;
    protected final Timer moveTimer;
    protected final Timer attackTimer;
    protected Image attackImage;
    protected java.util.Timer enemyAttacked;
    protected Directions lastNoneStillDirection;
    protected boolean changeDirection;
    protected boolean canAttack;
    protected final int ATTACK_SPEED=1;
    protected Image enemyImages[];
    protected int EnemyType=0;


    private Boolean gotAttacked=false;

    public Enemy(int x, int y, int width, int height, Image images[], int damage, int visionRange, int attackRange, int healthPoints, float speed,int levelDepth, boolean isBoss) {
        super(x, y, width, height, images[0]);
        this.damage = damage;
        this.visionRange=visionRange;
        this.attackRange=attackRange;
        this.healthPoints=healthPoints;
        this.speed=speed;
        this.levelDepth=levelDepth;
        changeDirection=true;
        canAttack = true;
        moveTimer = new Timer();
        attackTimer = new Timer();
        moveTimer.schedule(new collideTask(), 0, 2000);
        lastMove = Directions.Still;
        lastNoneStillDirection = Directions.Left;
        this.enemyImages=images;
        Random randtype = new Random();
        if(!isBoss) {
            EnemyType = randtype.nextInt(3);
        }
        try
        {
            attackImage = ImageIO.read(this.getClass().getClassLoader().getResource("attack.png"));
        }
        catch (Exception e)
        {
            System.out.println("Missing enemy attack");
        }

    }

    public void takeDamage(int damage)
    {
        healthPoints-=damage;
        if(healthPoints<0)healthPoints=0;
    }

    public Attack attack(Player player)
    {
        Vector<Player> target = new Vector<>();
        target.removeAllElements();
        target.add(player);
        if(moveDirection!=Directions.Still) {
            return new Attack(x, y, 25, attackRange, attackImage, this, target, moveDirection, attackRange, damage);
        }
        else{
            return new Attack(x, y, 25, attackRange, attackImage, this, target, lastNoneStillDirection, attackRange, damage);
        }
    }

    public boolean isPlayerInVisionRange(int posX, int posY)
    {
        return visionRange>= Math.abs(x-posX)+ Math.abs(y-posY);
    }

    public void followPlayer(int posX, int posY)
    {
        int relX = x-posX;
        int relY = y-posY;
        if(Math.max(Math.abs(relX), Math.abs(relY))==Math.abs(relX))
        {
            if(relX>0)
            {
                moveDirection =Directions.Left;
            }
            else
            {
                moveDirection =Directions.Right;
            }
        }
        else
        {
            if(relY>0)
            {
                moveDirection =Directions.Up;
            }
            else
            {
                moveDirection =Directions.Down;
            }

        }
        lastNoneStillDirection=moveDirection;

    }

    public Attack behaviour(Player player)
    {
        if(isPlayerInVisionRange(player.getX(), player.getY()))
        {
            followPlayer(player.getX(), player.getY());
            if(canAttack)
            {
                canAttack=false;
                attackTimer.schedule(new attackedRecently(), 1000/ATTACK_SPEED);
                return attack(player);
            }
        }
        else
        {
            if(changeDirection) {
                changeDirection=false;
                moveTimer.schedule(new changeDir(), 250);
                randDirection();
            }
        }
        move();
        return null;
    }

    public void move() {

        switch (moveDirection) {
            case Up:
                y -= (int)speed;
                this.image=enemyImages[0+(EnemyType*4)];
                break;
            case Down:
                y += (int)speed;
                this.image=enemyImages[1+(EnemyType*4)];
                break;
            case Left:
                x -= (int)speed;
                this.image=enemyImages[2+(EnemyType*4)];
                break;
            case Right:
                x += (int)speed;
                this.image=enemyImages[3+(EnemyType*4)];
                break;
            case Still:
                break;
        }
        lastMove = moveDirection;
    }


    public void randDirection() {
        Random rand = new Random();
        int randD = rand.nextInt(5);
        switch (randD) {
            case 0:
                moveDirection = Directions.Up;
                break;
            case 1:
                moveDirection = Directions.Down;
                break;
            case 2:
                moveDirection = Directions.Left;
                break;
            case 3:
                moveDirection = Directions.Right;
                break;
            case 4:
                moveDirection = Directions.Still;
                break;
        }
        if(moveDirection!=Directions.Still)lastNoneStillDirection=moveDirection;
    }


    public void moveBack() {
        switch (lastMove) {
            case Up:
                y += (int)speed;
                break;
            case Down:
                y -= (int)speed;;
                break;
            case Left:
                x += (int)speed;
                break;
            case Right:
                x -= (int)speed;
                break;
            case Still:
                break;
        }
    }


    public Item dropLoot(Player player)
    {
        Random rand = new Random();
        int experience=10 + 15*levelDepth;
        int money = 5 + 8*levelDepth;
        player.giveExperience(experience);
        player.giveMoney(money);

        if(rand.nextInt(10)==2) //the chance of it landing on any single value should be equal at 10%
        {
            ItemGenerator generator = new ItemGenerator();
            return generator.generateSomething(levelDepth,false);
        }
        return null;

    }


    public Boolean damaged(float dmg,float attack_speed)
    {
        this.healthPoints-=dmg;
        enemyAttacked = new java.util.Timer();
        enemyAttacked.schedule(new gotDamagedTask(),(int)(1000/attack_speed));
        if(this.healthPoints<=0)
        {
            //died
            this.healthPoints=0;
            return true;
        }
        else
        {
            return false;
        }
    }

    //Getterek és szetterek az enemyhez


    public Directions getDirection() {
        return moveDirection;
    }

    public void setDirection(Directions direction) {
        this.moveDirection = direction;
    }

    public Directions getLastNoneStillDirection() {
        return lastNoneStillDirection;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Directions getLastMove() {
        return lastMove;
    }

    public void setLastMove(Directions lastMove) {
        this.lastMove = lastMove;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVelx() {
        return velx;
    }

    public void setVelx(int velx) {
        this.velx = velx;
    }

    public int getVely() {
        return vely;
    }

    public void setVely(int vely) {
        this.vely = vely;
    }

    public Boolean getGotAttacked()
    {
        return gotAttacked;
    }

    public void setGotAttacked(Boolean gotAttacked)
    {
        this.gotAttacked = gotAttacked;
    }

    class collideTask extends TimerTask
    {
        public void run()
        {
            randDirection();
        }
    }

    class gotDamagedTask extends TimerTask
    {
        public void run()
        {
            gotAttacked = false;
        }
    }

    class changeDir extends TimerTask
    {
        public void run()
        {
            changeDirection = true;
        }
    }
    class attackedRecently extends TimerTask
    {
        public void run()
        {
            canAttack = true;
        }
    }

}