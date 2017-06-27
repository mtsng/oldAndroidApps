package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
  Created by Jack on 5/21/2015.
 */
public class qBlock extends abstractBlock {
    //item blocks
    private int power;
    private int time;

    public qBlock(Bitmap qblock, MarioSurfaceView msv, int i, int y1, int y2, int power, int time) {
        super.x1 = i * msv.getWidth() / 25;
        super.x2 = i * msv.getWidth() / 25 + msv.getWidth() / 25;
        super.y1 = msv.getHeight() - y1;
        super.y2 = msv.getHeight() - y2;
        super.dst = new Rect(x1, y1, x2, y2);
        super.blocktype = 2;
        this.power = power;
        super.block = qblock;
        super.msv = msv;
        this.time = time;
    }

    public void vomitItem(WorldCreator w) {

        if (power == 1){
            w.items.add(new Boot(x1, y1 - 30, x1 + 50, y1, msv, time));
        }
        else {
            w.items.add(new Sword(x1, y1 - 50, x1 + 80, y1, msv, time));
        }
    }
}
