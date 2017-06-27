package com.eecs40.homework_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;

import java.io.InputStream;

/**
  Created by Michael on 5/21/2015.
 */
public class World2 extends WorldCreator{
    private InputStream ise1;
    private InputStream cuc;
    private Movie goomba;
    private Movie cucco;
    private int y;

    public World2(MarioSurfaceView msv, Context context){
        this.y = msv.getWidth()/25 - 1;
        super.endofstage = 2000;
        super.context = context;
        super.msv = msv;
        super.block = BitmapFactory.decodeResource(msv.getResources(), R.drawable.block, super.options);
        super.qblock = BitmapFactory.decodeResource(msv.getResources(), R.drawable.qblock, super.options);
        super.step = BitmapFactory.decodeResource(msv.getResources(), R.drawable.step, super.options);
        super.floor = BitmapFactory.decodeResource(msv.getResources(), R.drawable.floor, super.options);
        super.castle = BitmapFactory.decodeResource(msv.getResources(), R.drawable.castle, super.options);
        ise1 = context.getResources().openRawResource(+R.drawable.goomba);
        goomba = Movie.decodeStream(ise1);
        cuc = context.getResources().openRawResource(+R.drawable.cuccol);
        cucco = Movie.decodeStream(cuc);
    }

    public void InitializeWorld(){
        addFloors();
        addBlocks();
        addSteps();
        addEnemies();
        addItems();
        addRupees();
    }

    @Override
    protected void addFloors(){

        for (int i = 0 ; i < 2 ; i++){
            for (int j = 0 ; j < 60 ; j++){
                super.blocks.add(new Floor(floor, msv, j,(2-i)*y, (1-i)*y ));
            }
        }
        //provide the baseline of the level
        super.base = super.blocks.get(0).y1;
    }

    @Override
    protected void addBlocks(){
        for(int i = 0;i<5;i++){
            super.blocks.add(new Block(block, msv, i+6, 6*y, 5*y));
        }
        super.blocks.add(new qBlock( qblock, msv, 11, 6*y, 5*y, 1, 200));
        super.blocks.add(new qBlock( qblock, msv, 5, 6*y, 5*y, 2, 500));
    }

    @Override
    protected  void addSteps(){
        super.blocks.add(new Step(step, msv, 5, 3*y, 2*y));
        super.blocks.add(new Step(step, msv, 0, 3*y, 2*y));
        super.blocks.add(new Step(step, msv, 16, 3*y, 2*y));
        super.blocks.add(new Step(step, msv, 16, 4*y, 3*y));
        super.blocks.add(new Step(step, msv, 35, 3*y, 2*y));
        super.blocks.add(new Step(step, msv, 35, 4*y, 3*y));
    }

    protected void addEnemies(){
        for (int i = 0 ; i < 20 ; i++){
            super.enemies.add(new Goomba(msv, goomba));
            enemies.get(i).x = (900 + i*50) - goomba.width();
            enemies.get(i).y = base - goomba.height();
        }
        super.enemies.add(new Cucco(msv, cucco));
        enemies.get(enemies.size()-1).x = (1500);
        enemies.get(enemies.size()-1).y = (msv.getHeight() - 500) - cucco.height();

    }

    protected void addItems(){
    }

    protected void addRupees(){
        for(int i=25;i<35;i++){
            super.coins.add(new Coins(msv, i, 190, 240, super.options));
        }
    }

    protected void stageEnd(){
        //progress to next stage
        if(msv.link.x > endofstage){
           // System.out.println("stage end");
            msv.worldEnd(2);
        }
        //restart world with points
        if(msv.link.youdied){
            msv.restartWorld(2);
            msv.link.youdied = false;
        }
    }

    protected void draw(Canvas c){
        // setting.draw(c);
        super.draw(c);
        for(Enemies e:enemies){
            //test enemy collision in relation to the world
            e.testCollide(this);
        }
    }
}
