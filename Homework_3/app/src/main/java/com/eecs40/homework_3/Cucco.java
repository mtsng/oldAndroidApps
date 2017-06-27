package com.eecs40.homework_3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 5/15/2015.
 */
public class Cucco extends Enemies implements TimeConscious{
    private int vector = 3;

    public Cucco(MarioSurfaceView msv, Movie enemy){
        super.msv = msv;
        super.eH = enemy.height();
        super.eW = enemy.width();
        super.Enemy = enemy;
    }

    //overrides inherited horizontal behavior for vertical movement
    @Override
    protected void testCollide(WorldCreator w){
        enemyBox = new Rect(x, y, x + eW, y + eH);
        for(abstractBlock b:w.blocks) {
            if(b.dst.contains(Cx() + eW/4, By()) || b.dst.contains(Cx() - eW/4, By())){
                super.collide = true;
                vector = -3;
                //System.out.println("yes");
            }
            else if(b.dst.contains(Cx() + eW/4, y) || b.dst.contains(Cx() - eW/4, y)){
                super.collide = true;
                vector = 3;
            }
            else if(y < 0){
                vector = 3;
                super.collide = true;
            }
            else if(y + super.eH > msv.getHeight()){
                vector = -3;
                super.collide = true;
            }
        }
    }

    @Override
    public void tick(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        super.tick(c);
        y = (isKilled? y : (collide? y + vector : y - vector));
    }
}
