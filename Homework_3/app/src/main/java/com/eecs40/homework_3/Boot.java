package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
  Created by Michael on 5/21/2015.
 */
public class Boot extends Items{
    private int time;

    public Boot(int x1, int y1, int x2, int y2, MarioSurfaceView msv, int time){
        super.msv = msv;
        super.Lx = x1;
        super.Ly = y1;
        this.time = time;
        Rx = x2;
        Ry = y2;
        item = BitmapFactory.decodeResource(msv.getResources(), R.drawable.boots, options);
    }

    //Overload constructor for doemer image
    public Boot(int x1, int y1, int x2, int y2, MarioSurfaceView msv, int time, Bitmap d){
        super.msv = msv;
        super.Lx = x1;
        super.Ly = y1;
        this.time = time;
        Rx = x2;
        Ry = y2;
        item = d;
    }

    //item collision check with set up for speed up buff
    public void itemCollide(){
        super.itemCollide();
        if(gotten && obtained) {
            msv.link.setSpeed(time);
            obtained = false;
        }
    }
}
