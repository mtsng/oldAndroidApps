package com.example.michael.myapplication;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Michael on 4/6/2015.
 */
public class BubbleShooterThread extends Thread{
    BubbleShooterView bsv;

    public BubbleShooterThread(BubbleShooterView bsv){
        this.bsv = bsv;
    }

    public void run(){
        SurfaceHolder sh = bsv.getHolder();

        //Main game loop
        while(!Thread.interrupted()){
            Canvas c = sh.lockCanvas(null);
            try{
                synchronized(sh){
                    bsv.advanceFrame(c);
                }
            }
            catch(Exception e){

            }
            finally{
                if(c != null){
                    sh.unlockCanvasAndPost(c);
                }
            }
            //Set the frame rate by setting this delay
            try{
                Thread.sleep(5);
            }
            catch(InterruptedException e){
                //Thread was interrupted while sleeping
                return;
            }
        }

    }
}
