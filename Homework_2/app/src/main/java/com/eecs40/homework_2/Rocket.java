package com.eecs40.homework_2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 4/22/2015.
 */
public class Rocket implements TimeConscious{
    private int alpha;
    private double dalpha;
    public int hits = 5;
    public int VelY;
    public double fVelY;
    private final DashTillPuffSurfaceView dtpv;
    private final Bitmap rocket;
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public int ga;
    private double g = 0.5;
    private double accel = -0.75;
    public int lose;
    private double AlphaStep = 5.0;

    public Rocket(DashTillPuffSurfaceView dtpv, Bitmap rocket, int w, int h){
        this.rocket = rocket;
        this.dtpv = dtpv;
        this.VelY = 1;
        this.fVelY = VelY/1.0;
        this.x1 = w - 400;
        this.x2 = w - 300;
        this.y1 = h - 100;
        this.y2 = h;
        this.ga = 0;
        this.alpha = 255;
        this.dalpha = 255.0;
        this.lose = 0;
    }

    private void draw(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(alpha);
        Rect dst= new Rect(x1, y1, x2, y2);
        c.drawBitmap(rocket, null, dst, paint);
    }

    //Draw swipe/hit counter
    public void hits(Canvas c, String s){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(dtpv.getWidth()/24);
        c.drawText(s, dtpv.getWidth()/80, dtpv.getHeight()/12, paint);
    }

    public void lost(Canvas c, String s){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(dtpv.getWidth()/8);
        c.drawText(s, (int)(dtpv.getWidth()/5.5), 4*dtpv.getHeight()/7, paint);
    }

    @Override
    public void tick(Canvas c){
        draw(c);
        //System.out.println("Space ship is at y: " + y1);
        y1 += VelY;
        y2 += VelY;

        if (lose == 1 && hits > 0) {
            if (alpha <= 0){
                lose = 0;
                alpha = 255;
                dalpha = 255.0;
                hits -= 1;
            }
            dalpha -= AlphaStep;
            alpha = (int)dalpha;
        }
        if (hits == 0){
            alpha = 5;
            dalpha = 5.0;
        }
        if(ga == 1 && y1 > 4){
            fVelY = fVelY + accel;
        }
        if(ga == 0 && y2 < (dtpv.getHeight()-4)){
            fVelY = fVelY + g;
        }

        VelY = (int)fVelY;
        //forces the ship to keep within the lower bounds of the screen
        if(y2 > dtpv.getHeight()){
            y1 = dtpv.getHeight() - 100;
            y2 = dtpv.getHeight();
            VelY = 0;
            fVelY = 0;
        }
        //forces the ship to keep within the upper bounds of the screen
        else if(y1 < 0){
            y1 = 0;
            y2 = 100;
            VelY = 0;
            fVelY = 0;
        }
    }
}
