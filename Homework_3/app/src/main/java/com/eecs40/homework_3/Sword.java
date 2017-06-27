package com.eecs40.homework_3;

import android.graphics.BitmapFactory;

/**
 Created by Michael on 5/21/2015.
 */
public class Sword extends Items{
    private int time;

    public Sword(int x1, int y1, int x2, int y2, MarioSurfaceView msv, int time){
        super.msv = msv;
        super.Lx = x1;
        super.Ly = y1;
        this.time = time;
        Rx = x2;
        Ry = y2;
        item = BitmapFactory.decodeResource(msv.getResources(), R.drawable.sword, options);

    }

    //set invincibility mode
    public void itemCollide(){
        super.itemCollide();
        if(gotten && obtained) {
            msv.link.setSword(time);
            obtained = false;
        }
    }
}
