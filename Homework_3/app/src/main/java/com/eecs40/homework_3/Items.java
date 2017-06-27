package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 5/21/2015.
 */
abstract public class Items implements TimeConscious{
    protected Bitmap item;
    public Rect dst;
    protected MarioSurfaceView msv;
    BitmapFactory.Options options = new BitmapFactory.Options();
    protected boolean gotten;
    protected boolean obtained;
    protected int Lx;
    protected int Rx;
    protected int Ly;
    protected int Ry;

    private void draw(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        if(!gotten) {
            dst = new Rect(Lx, Ly, Rx, Ry);
            c.drawBitmap(item, null, dst, paint);
        }
    }

    public void itemCollide(){
        int rX = msv.link.Cx() + msv.link.gWidth()/2;
        int rY = msv.link.Cy() + msv.link.gHeight()/2;
        int lX = msv.link.x;
        int lY = msv.link.y;

        //check for obtaining item
        if(dst != null && (dst.contains(rX, rY - 3) || dst.contains(lX, lY - 3) || dst.contains(lX, rY) || dst.contains(rX, lY))){
            dst = null;
            gotten = true;
            obtained = true;
        }
    }

    public void tick(Canvas c){
        draw(c);
    }

}
