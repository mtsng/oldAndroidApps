package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Rect;

/**
  Created by Michael on 5/13/2015.
 */
public class Goomba extends Enemies implements TimeConscious{

    public Goomba(MarioSurfaceView msv, Movie enemy){
        super.msv = msv;
        super.Enemy = enemy;
        super.eH = enemy.height();
        super.eW = enemy.width();
        super.death = BitmapFactory.decodeResource(msv.getResources(), R.drawable.gstomped, super.options);
    }

    @Override
    public void tick(Canvas c){
        super.tick(c);
        x = (isKilled? x : (collide? x + 3 : x - 3));
    }


}
