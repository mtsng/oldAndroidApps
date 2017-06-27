package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 5/12/2015.
 */
public class Background implements TimeConscious{
    private int x1;
    private int y1;
    public int x2;
    public int y2;
    private final MarioSurfaceView msv;
    private final Bitmap setting;

    public Background(MarioSurfaceView msv, Bitmap bitmap){
        this.setting = bitmap;
        this.msv = msv;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
    }

    public int SizeWidth(){ return msv.getWidth(); }

    public int SizeHeight(){ return msv.getHeight(); }

    private void draw(Canvas c){
        Paint paint = new Paint();
        Rect dst = new Rect(x1, y1, x2, y2);
        paint.setColor(Color.WHITE);
        c.drawBitmap(setting, null, dst, paint);
    }

    public void tick(Canvas c){

        draw(c);
    }

}

