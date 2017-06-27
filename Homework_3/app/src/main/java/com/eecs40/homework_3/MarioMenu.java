package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 5/16/2015.
 */
public class MarioMenu implements TimeConscious{
    //start menu
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private final MarioSurfaceView msv;
    private final Bitmap setting; //background
    private final Bitmap sButton; //start button
    private final Bitmap tbutton; //tutorial button

    public MarioMenu(MarioSurfaceView msv, BitmapFactory.Options opt){
        this.setting = BitmapFactory.decodeResource(msv.getResources(), R.drawable.smbmenu, opt);
        this.msv = msv;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = msv.getWidth();
        this.y2 = msv.getHeight();
        this.sButton = BitmapFactory.decodeResource(msv.getResources(), R.drawable.startbutton, opt);
        this.tbutton = BitmapFactory.decodeResource(msv.getResources(), R.drawable.tbutton, opt);
    }

    public int SizeWidth(){ return msv.getWidth(); }

    public int SizeHeight(){ return msv.getHeight(); }

    private void draw(Canvas c){
        Paint paint = new Paint();
        Rect dst = new Rect(x1, y1, x2, y2);
        Rect dst2 = new Rect(SizeWidth()/2 - 100, SizeHeight() -  200, SizeWidth()/2 + 100, SizeHeight() - 15);
        Rect dst3 = new Rect(SizeWidth()/2 + 200, SizeHeight() -  150, SizeWidth()/2 + 400, SizeHeight() - 50);
        paint.setColor(Color.BLACK);
        c.drawBitmap(setting, null, dst, paint);
        c.drawBitmap(sButton, null, dst2, paint);
        c.drawBitmap(tbutton, null, dst3, paint);
    }

    public void tick(Canvas c){

        draw(c);
    }
}
