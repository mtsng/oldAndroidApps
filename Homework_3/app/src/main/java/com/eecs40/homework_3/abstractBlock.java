package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Jack on 5/16/2015.
 */
//generalized class for blocks in the world
abstract public class abstractBlock implements TimeConscious{
    public int y1;
    public int y2;
    public int x1;
    public int x2;
    protected MarioSurfaceView msv;
    public Rect dst;
    public int blocktype;
    protected Bitmap block;
    BitmapFactory.Options options = new BitmapFactory.Options();

    //centerx
    public int Cx(){
        return x1 + (x2 - x1)/2;
    }

    //centery
    public int Cy(){
        return y1 + (y2 - y1)/2;
    }

    private void draw(Canvas c){
        Paint paint = new Paint();
        dst = new Rect(x1, y1, x2, y2);
        //only draws objects on screen to optimize game
        if (x1 < 1500 && x2 > 0){
            c.drawBitmap(block, null, dst, paint);
        }
    }
    public void tick(Canvas c){
        draw(c);
    }
}
