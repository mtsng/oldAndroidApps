package com.eecs40.homework_3;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
  Created by Michael on 5/11/2015.
 */
public class MarioThread extends Thread{
    private final MarioSurfaceView msv;
    private static final int FRAMES = 5;

    public MarioThread(MarioSurfaceView msv){
        this.msv = msv;
    }

    public void run(){
        SurfaceHolder sh = msv.getHolder();

        //Main loop
        while(!Thread.interrupted()){
            Canvas c = sh.lockCanvas(null);
            try{
                synchronized (sh){
                    msv.tick(c);
                }
            }
            catch(Exception e){

            }
            finally{
                if(c != null){
                    sh.unlockCanvasAndPost(c);
                }
            }
            try{
                Thread.sleep(FRAMES);
            }
            catch(InterruptedException e){
                return;
            }
        }

    }

}
