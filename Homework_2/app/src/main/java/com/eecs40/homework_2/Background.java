package com.eecs40.homework_2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 4/20/2015.
 */
public class Background implements TimeConscious{
    public  int x1;
    public  int y1;
    public  int x2;
    public  int y2;
    private final DashTillPuffSurfaceView dtpv;
    private final Bitmap setting;

    public Background(DashTillPuffSurfaceView dtpv, Bitmap bitmap){
        this.setting = bitmap;
        this.dtpv = dtpv;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
    }

    public int SizeWidth(){
        return dtpv.getWidth();
    }

    public int SizeHeight(){
        return dtpv.getHeight();
    }

    private void draw(Canvas c){
        Paint paint = new Paint();
        Rect dst = new Rect(x1, y1, x2, y2);
        paint.setColor(Color.WHITE);
        //draws background bitmap
        c.drawBitmap(setting, null, dst, paint);
    }

    public void tick(Canvas c){
        draw(c);
        x1 += 3;
        x2 += 3;
        //when on background in past the screen, it re-draws it back in its initial position
        if(x1 > SizeWidth()){
            x1 = -SizeWidth();
            x2 = 10;
        }
    }
}
