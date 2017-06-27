package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Michael on 5/13/2015.
 */
public class Floor extends abstractBlock{
    //floor blocks
    public Floor(Bitmap floor, MarioSurfaceView msv, int i, int y1, int y2){
        super.block = floor;
        super.msv = msv;
        super.x1 = i*msv.getWidth()/25 - 1;
        super.x2 = i*msv.getWidth()/25 + msv.getWidth()/25;
        super.y1 = msv.getHeight() - y1;
        super.y2 = msv.getHeight() - y2;
        super.dst = new Rect(x1, y1, x2, y2);
        super.blocktype = 3;
    }
}
