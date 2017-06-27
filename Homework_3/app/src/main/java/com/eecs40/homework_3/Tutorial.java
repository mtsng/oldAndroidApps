package com.eecs40.homework_3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.InputStream;

/**
  Created by Michael on 5/26/2015.
 */
public class Tutorial implements TimeConscious {
    //tutorial screen
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private final MarioSurfaceView msv;
    private final Bitmap tscreen;
    private final Bitmap bButton;

    public Tutorial(MarioSurfaceView msv, BitmapFactory.Options opt){
        this.tscreen = BitmapFactory.decodeResource(msv.getResources(), R.drawable.tutorial, opt);
        this.msv = msv;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = msv.getWidth();
        this.y2 = msv.getHeight();
        this.bButton = BitmapFactory.decodeResource(msv.getResources(), R.drawable.bbutton, opt);
    }

    public int SizeWidth(){ return msv.getWidth(); }

    public int SizeHeight(){ return msv.getHeight(); }

    private void draw(Canvas c){
        Paint paint = new Paint();
        Rect dst = new Rect(x1, y1, x2, y2);
        Rect dst2 = new Rect(0, SizeHeight() - 100, SizeWidth()/10, SizeHeight());
        paint.setColor(Color.BLACK);
        c.drawBitmap(tscreen, null, dst, paint);
        c.drawBitmap(bButton, null, dst2, paint);
    }

    public void tick(Canvas c){
        draw(c);
    }
}
