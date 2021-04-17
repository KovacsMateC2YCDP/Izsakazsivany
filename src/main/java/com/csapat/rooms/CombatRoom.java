package com.csapat.rooms;

import com.csapat.entity.Enemy;
import com.csapat.entity.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Random;
import java.util.Vector;

/**
 * <h1>CombatRoom</h1>
 * contains 3-7 enemies, all of which have random stats that scale with levelDepth
 */
public class CombatRoom extends Room
{

    Random rand= new Random();
    private int enemyCount;
    private Vector<Item> items;


    public CombatRoom(int levelDepth)
    {
        super(levelDepth);
        items = new Vector<>();
        enemyCount=rand.nextInt(4)+3;
        enemies= new Vector<Enemy>();
        this.roomSpecificGen();
        this.levelDepth=levelDepth;
    }

    @Override
    public void roomSpecificGen()
    {
        Image enemyTexture=null;
        try
        {
            enemyTexture = ImageIO.read(this.getClass().getClassLoader().getResource("enemy.png"));
        }
        catch(Exception e){System.out.println("Enemy texture is missing");}
        for(int i=0;i<enemyCount;++i)
        {
            int healthPoints = rand.nextInt(20)+50 + (levelDepth-1)*15 ;
            float moveSpeed = 2.5f + (levelDepth-1)*0.4f ;
            int visionRange = rand.nextInt(125)+25 + (levelDepth-1)*50; //enemies have full vision of every room from depth12
            int attackRange = rand.nextInt(20) + 40 + (levelDepth-1)*20 ;
            int damage = Math.round(rand.nextInt(10)+20 +(levelDepth-1)*15);

            enemies.add(new Enemy(/*getN() /2*/ 200 +i*50 ,/*getM()/2*/ 200  ,50,50,enemyTexture,damage,visionRange,attackRange,healthPoints,moveSpeed,levelDepth));



        }
    }
    public void removeItem(Item item)
    {
        for (Item it: items)
        {
            if(it.equals(item))
            {
                items.remove(item);
                break;
            }
        }
    }

    public Vector<Item> getItems(){return items;}
    //does a combat room prevent you from leaving while enemies are alive?
    //if so, make that


}
