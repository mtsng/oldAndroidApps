package com.eecs40.homework_3;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;

import java.io.InputStream;

/**
 Created by Michael on 5/13/2015.
 */
public class World1 extends WorldCreator implements TimeConscious {
    private InputStream ise1;
    private InputStream cuc;
    private Movie goomba;
    private Movie cucco;
    private int y;

    public World1(MarioSurfaceView msv, Context context){
        super.qblock = BitmapFactory.decodeResource(msv.getResources(), R.drawable.qblock, super.options);
        super.block = BitmapFactory.decodeResource(msv.getResources(), R.drawable.block, super.options);
        super.step = BitmapFactory.decodeResource(msv.getResources(), R.drawable.step, super.options);
        super.floor = BitmapFactory.decodeResource(msv.getResources(), R.drawable.floor, super.options);
        super.castle = BitmapFactory.decodeResource(msv.getResources(), R.drawable.castle, super.options);
        super.msv = msv;
        super.endofstage = 3300;
        super.context = context;
        this.y = msv.getWidth()/25 - 1;
        ise1 = context.getResources().openRawResource(+R.drawable.goomba);
        goomba = Movie.decodeStream(ise1);
        cuc = context.getResources().openRawResource(+R.drawable.cuccol);
        cucco = Movie.decodeStream(cuc);
    }

    @Override
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
       //Main floor
       for (int i = 0 ; i < 2 ; i++){
           for (int j = 0 ; j < 75 ; j++){
               //Big gap for jumping challenge
               if (j < 26 || j > 50){
                   super.blocks.add(new Floor(floor, msv, j,(2-i)*y, (1-i)*y));
               }
           }
       }
       //Blocks to fill gap
       super.blocks.add(new Floor(floor, msv, 38, 3*y, 2*y));
       super.blocks.add(new Floor(floor, msv, 39, 3*y, 2*y));
       super.blocks.add(new Floor(floor, msv, 39, 4*y, 3*y));
       super.blocks.add(new Floor(floor, msv, 43, 3*y, 2*y));
       super.blocks.add(new Floor(floor, msv, 42, y, 0));
       super.blocks.add(new Floor(floor, msv, 47, 3*y, 2*y));
       super.blocks.add(new Floor(floor, msv, 46, y, 0));
       super.blocks.add(new Floor(floor, msv, 50, y, 0));

       //Get base
       super.base = super.blocks.get(0).y1;
    }

    @Override
    protected void addBlocks(){
        //Fill in blocks at the beginning
        for(int i = 0;i<5;i++){
            super.blocks.add(new Block(block, msv, i+6, 6*y, 5*y));
        }

        //add power blocks (a.k.a question mark Blocks)
        //powers: 1. speed mode; 2. sword mode (final parameter is power time))
        super.blocks.add(new qBlock( qblock, msv, 11, 6*y, 5*y, 1, 50));
        super.blocks.add(new qBlock( qblock, msv, 5, 6*y, 5*y, 2, 500));
    }

    @Override
    protected  void addSteps(){
        //Design blocks at the beginning.
        //one to mark beginning and one to stand on to avoid goombas
        super.blocks.add(new Step(step, msv, 5, 3*y, 2*y));
        super.blocks.add(new Step(step, msv, 0, 3*y, 2*y));


        //Two walls
        for (int i = 3 ; i < 12; i++){
            super.blocks.add(new Step(step, msv, 16, i*y, (i-1)*y));
            super.blocks.add(new Step(step, msv, 25, i*y, (i-1)*y));
        }

        //Jumping blocks inside walls
        super.blocks.add(new Step(step, msv, 24, 6*y, 5*y));
        super.blocks.add(new Step(step, msv, 21, 8*y, 7*y));
        super.blocks.add(new Step(step, msv, 24, 10*y, 9*y));

        //Two blocks between walls
        for(int i = 0;i<2;i++){
            super.blocks.add(new Step(step, msv, i+20, 3*y, 2*y));
        }

        //Stepping down blocks
        super.blocks.add(new Step(step, msv, 28, 9*y, 8*y));
        super.blocks.add(new Step(step, msv, 29, 9*y, 8*y));
        super.blocks.add(new Step(step, msv, 32, 7*y, 6*y));
        super.blocks.add(new Step(step, msv, 35, 5*y, 4*y));

        //Pyramid stairs near end
        for (int i = 0 ; i < 10 ; i ++){
            for (int j = 0 ; j < i ; j++){
                super.blocks.add(new Step(step, msv, 53 + i, (3+j)*y, (2+j)*y));
            }
        }
    }

    @Override
    protected void addEnemies(){

        //Two goombas in the beginning
        for (int i = 0 ; i < 2 ; i++){
            super.enemies.add(new Goomba(msv, goomba));
            enemies.get(i).x = (msv.getWidth() - 600 + i*100) - goomba.width();
            enemies.get(i).y = base - goomba.height();
        }

        //Two goombas between walls
        for (int i = 2 ; i < 4 ; i++) {
            super.enemies.add(new Goomba(msv, goomba));
            enemies.get(i).x = (msv.getWidth() - 50 - i * 100) - goomba.width();
            enemies.get(i).y = base - goomba.height();
        }

        //Four cuccos to make jumping through more difficult
        super.enemies.add(new Cucco(msv, cucco));
        enemies.get(enemies.size()-1).x = (1150);
        enemies.get(enemies.size()-1).y = (msv.getHeight() - 500) - cucco.height();
        super.enemies.add(new Cucco(msv, cucco));
        enemies.get(enemies.size()-1).x = (1340);
        enemies.get(enemies.size()-1).y = (msv.getHeight() - 450) - cucco.height();
        super.enemies.add(new Cucco(msv, cucco));
        enemies.get(enemies.size()-1).x = (1565);
        enemies.get(enemies.size()-1).y = (msv.getHeight() - 400) - cucco.height();
        super.enemies.add(new Cucco(msv, cucco));
        enemies.get(enemies.size()-1).x = (1700);
        enemies.get(enemies.size()-1).y = (msv.getHeight() - 350) - cucco.height();
    }

    @Override
    protected void addItems(){
        //There will be no extra items in this level
    }

    @Override
    protected void addRupees(){
        //two rupees before first wall
        super.coins.add(new Coins(msv, 120, 120, 150, 160, super.options));
        super.coins.add(new Coins(msv, 180, 120, 210, 160, super.options));

        //one within walls
        super.coins.add(new Coins(msv, 1010, 155, 1040, 195, super.options));

        //three on steps down
        super.coins.add(new Coins(msv, 1390, 110, 1420, 150, super.options));
        super.coins.add(new Coins(msv, 1540, 195, 1570, 235, super.options));
        super.coins.add(new Coins(msv, 1685, 285, 1715, 325, super.options));

        //four on following bricks
        super.coins.add(new Coins(msv, 1820, 390, 1850, 430, super.options));
        super.coins.add(new Coins(msv, 2010, 480, 2040, 520, super.options));
        super.coins.add(new Coins(msv, 2200, 480, 2230, 520, super.options));
        super.coins.add(new Coins(msv, 2390, 480, 2420, 520, super.options));

        //hard to get after stairs
        super.coins.add(new Coins(msv, 3180, 200, 3210, 240, super.options));
    }

    @Override
    protected void stageEnd(){
        //progress to next stage
        if(msv.link.x > endofstage){
            msv.worldEnd(1);
        }

        //restart world with points
        if(msv.link.youdied){
            msv.restartWorld(1);
            msv.link.youdied = false;
        }
    }

    @Override
    protected void draw(Canvas c){
       // implementing draw
        super.draw(c);
        for(Enemies e:enemies){
            e.testCollide(this);
        }
    }
}
