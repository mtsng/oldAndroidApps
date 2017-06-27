package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 5/24/2015.
 */
public class Lives {
    public Rect dst;
    public int x1;
    private int y1;
    public int x2;
    private int y2;
    private Bitmap heart;
    private final MarioSurfaceView msv;

    public Lives(MarioSurfaceView msv, int x1, int y1, int x2, int y2, BitmapFactory.Options opt){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.msv = msv;
        this.heart = BitmapFactory.decodeResource(msv.getResources(), R.drawable.heart, opt);
    }

    public void draw(Canvas c){
        Paint paint = new Paint();
        dst = new Rect(x1, y1, x2, y2);
        c.drawBitmap(heart, null, dst, paint);
    }

}
