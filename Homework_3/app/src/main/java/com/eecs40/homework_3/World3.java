package com.eecs40.homework_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;

import java.io.InputStream;

/**
  Created by Michael on 5/24/2015.
 */
public class World3 extends WorldCreator{
    private InputStream ise1;
    private InputStream cuc;
    private Movie goomba;
    private Movie cucco;
    private Enemies e1;
    private int y;
    private Bitmap doemer;

    public World3(MarioSurfaceView msv, Context context){
        super.context = context;
        super.msv = msv;
        super.block = BitmapFactory.decodeResource(msv.getResources(), R.drawable.block, super.options);
        super.step = BitmapFactory.decodeResource(msv.getResources(), R.drawable.step, super.options);
        super.floor = BitmapFactory.decodeResource(msv.getResources(), R.drawable.floor, super.options);
        super.castle = BitmapFactory.decodeResource(msv.getResources(), R.drawable.castle, super.options);
        this.doemer = BitmapFactory.decodeResource(msv.getResources(), R.drawable.domer, super.options);
        this.y = msv.getWidth()/25 - 1; //50
        super.endofstage = 3000;
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
            for (int j = 0 ; j < 70 ; j++){
                super.blocks.add(new Floor(floor, msv, j, (2-i)*y, (1-i)*y));
            }
        }
        super.base = super.blocks.get(0).y1;
    }

    @Override
    protected void addBlocks(){
        for(int i = 0;i<5;i++){
            super.blocks.add(new Block(block, msv, i+6, 6*y, 6*y - y));
        }
        for(int i = 0;i<3;i++){
            super.blocks.add(new Block(block, msv, i+13, 6*y, 6*y - y));
        }
    }

    @Override
    protected  void addSteps(){
        for(int i = 0;i<5;i++){
            super.blocks.add(new Step(step, msv, i+20, 3*y, 2*y));
        }
        for(int i=0;i<4;i++){
            super.blocks.add(new Step(step, msv, i+21, 4*y, 3*y));
        }
        for(int i=0;i<3;i++){
            super.blocks.add(new Step(step, msv, i+22, 5*y, 4*y));
        }
        for(int i=0;i<2;i++){
            super.blocks.add(new Step(step, msv, i+23, 6*y, 5*y));
        }
        super.blocks.add(new Step(step, msv, 24, 7*y, 6*y));
        super.blocks.add(new Step(step, msv, 0, 3*y, 2*y));
        super.blocks.add(new Step(step, msv, 0, 4*y, 3*y));
    }

    protected void addEnemies(){
        for (int i = 0 ; i < 3 ; i++){
            super.enemies.add(new Goomba(msv, goomba));
            enemies.get(i).x = (msv.getWidth() - 600 + i*100) - goomba.width();
            enemies.get(i).y = base - goomba.height();
        }
        for (int i = 0 ; i < 30 ; i ++){
            super.enemies.add(new Cucco(msv, cucco));
            enemies.get(enemies.size()-1).x = (1300 + i * 50);
            enemies.get(enemies.size()-1).y = (msv.getHeight() - 100 - i*20) - cucco.height();
        }
    }

    protected void addItems(){
        //add doemer power up
        super.items.add(new Sword(220, 220, 300, 300, msv, 400));
        super.items.add(new Boot(220, 220, 300, 300, msv, 400, doemer));
    }

    protected void addRupees(){
        for(int i=45;i<60;i++){
            super.coins.add(new Coins(msv, i, 120, 170, super.options));
        }
    }

    protected void stageEnd(){
        //progress to next stage
        if(msv.link.x > endofstage){
           // System.out.println("stage end");
            msv.worldEnd(3);
        }
        //restart world with points
        if(msv.link.youdied){
            msv.restartWorld(3);
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
