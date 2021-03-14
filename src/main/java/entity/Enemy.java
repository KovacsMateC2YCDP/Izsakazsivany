package entity;

import entity.Sprite;

import java.awt.*;

public class Enemy extends Sprite
{
    private int healthPoints;
    private float moveSpeed;
    private int attackRange;
    private float damage;
    private String name;
    private int velx;
    private int vely;
    int walkingTime;
    Image EnemyImage[];

    public Enemy(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width=width;
        this.height= height;

    }




    //todo this functions
  public void behaviour(){

  }

 public void move(){
//random mászkáljon
     //sdsdh
  }
  public void attack(){
    //player.
      //attack enemy olyan mint mint a player
      //sprite collide overload
  }

  //Getterek az enemyhez

    public int getHealthPoints() {
        return healthPoints;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public float getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }

    public int getVelx() {
        return velx;
    }

    public int getVely() {
        return vely;
    }
}
