package com.eecs40.homework_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
  Created by Michael on 5/12/2015.
 */
abstract public class WorldCreator implements TimeConscious{
    public ArrayList<abstractBlock> blocks = new ArrayList<>();
    public ArrayList<Enemies> enemies = new ArrayList<>();
    public ArrayList<Items> items = new ArrayList<>();
    public ArrayList<Coins> coins = new ArrayList<>();
    protected MarioSurfaceView msv;
    protected Context context;
    protected Bitmap qblock;
    protected Bitmap castle;
    protected Bitmap block;
    protected Bitmap floor;
    protected Bitmap step;
    protected int endofstage;
    public int base;
    BitmapFactory.Options options = new BitmapFactory.Options();

    private int dx(){
        return msv.link.shiftWorld();
    }

    protected void draw(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        //draws steps, floor, qblocks, and blocks
        for(abstractBlock b:blocks) {
            if (b == null) {
                continue;
            }
            b.x1 -= dx();
            b.x2 -= dx();
            b.tick(c);
        }

        //Indicates the end of a stage
        endofstage -= dx();
        Rect dst1 = new Rect(endofstage - 50, base - 220, endofstage + 150, base);
        //draws a castle at the end of the stage
        if (endofstage < 1600){
            c.drawBitmap(castle, null, dst1, paint);
        }


        //draw enemies
        for(Enemies e:enemies){
            if(e == null){
                continue;
            }
            e.x -= dx();
            e.tick(c);
        }

        //draw items
        for(Items i:items){
            if(i == null){
                continue;
            }
            i.Lx -= dx();
            i.Rx -= dx();
            i.tick(c);
        }

        //draw coins
        for(Coins m:coins){
            if(m == null){
                continue;
            }
            m.x1 -= dx();
            m.x2 -= dx();
            m.tick(c);
        }
        msv.link.sShift = false;
    }

    abstract public void InitializeWorld();

    abstract protected void addFloors();

    abstract protected void addBlocks();

    abstract protected void addSteps();

    abstract protected void addEnemies();

    abstract protected void addItems();

    //allows progession to next world, or restart a world upon death
    abstract protected void stageEnd();

    //add coins
    abstract protected void addRupees();

    @Override
    public void tick(Canvas c){
        stageEnd();
        draw(c);
    }
}
