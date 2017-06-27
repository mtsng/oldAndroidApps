package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 5/23/2015.
 */
public class Coins implements TimeConscious{
    public Rect dst;
    public int x1;
    private int y1;
    public int x2;
    private int y2;
    private Bitmap coin;
    private final MarioSurfaceView msv;
    public final int points = 50;

    //constructor for inidividual coin placement
    public Coins(MarioSurfaceView msv, int x1, int y1, int x2, int y2, BitmapFactory.Options opt){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.msv = msv;
        this.coin = BitmapFactory.decodeResource(msv.getResources(), R.drawable.grupee, opt);
    }

    //constructor for rows of coins
    public Coins(MarioSurfaceView msv, int i, int y1, int y2, BitmapFactory.Options opt){
        this.x1 = i*msv.getWidth()/32;
        this.y1 = y1;
        this.x2 = i*msv.getWidth()/32 + 30;
        this.y2 = y2;
        this.msv = msv;
        this.coin = BitmapFactory.decodeResource(msv.getResources(), R.drawable.grupee, opt);
    }

    private void draw(Canvas c){
        Paint paint = new Paint();
        dst = new Rect(x1, y1, x2, y2);
        //only draws on screen items for optimization
        if (x1 < 1600 && x2 > - 50){
            c.drawBitmap(coin, null, dst, paint);
        }
    }

    public boolean coinCollide(){
        int rX = msv.link.Cx() + msv.link.gWidth()/2;
        int rY = msv.link.Cy() + msv.link.gHeight()/2;
        int lX = msv.link.x;
        int lY = msv.link.y;
        int Cx = msv.link.Cx();
        int Cy = msv.link.Cy();

        //check for obtaining coin
        if(dst != null && (dst.contains(Cx, Cy) || dst.contains(rX, rY) || dst.contains(lX, lY) || dst.contains(lX, rY) || dst.contains(rX, lY))){
            dst = null;
            //System.out.println("coin get");
            return true;
        }
        return false;
    }

    @Override
    public void tick(Canvas c) {
        draw(c);
    }
}
