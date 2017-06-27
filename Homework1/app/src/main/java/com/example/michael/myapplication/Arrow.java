package com.example.michael.myapplication;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
  Created by Michael on 4/14/2015.
 */
public class Arrow {
    private final BubbleShooterView bsv;
    public float x1, y1, x2, y2;

    public Arrow(BubbleShooterView bsv){
        this.bsv = bsv;
        this.x1 = 0;
        this.x2 = 0;
        this.y1 = 0;
        this.y2 = 0;
    }

    public void drawLine(Canvas c){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        c.drawLine(x1, y1, x2, y2, paint);
    }

}
