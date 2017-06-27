package com.eecs40.homework_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 Created by Michael on 4/20/2015.
 */
public class DashTillPuffSurfaceView extends SurfaceView implements SurfaceHolder.Callback, TimeConscious{
    private DashTillPuffRenderThread dtpv;
    public static Bitmap spaceship;
    public static Bitmap setting;
    private Trajectory trj;
    private Background sliding1;
    private Background sliding2;
    private Rocket ship;
    private CosmicFactory CFac;

    public DashTillPuffSurfaceView(Context context){
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        BitmapFactory.Options options = new BitmapFactory.Options();
        setting = BitmapFactory.decodeResource(this.getResources(), R.drawable.wallpaper1, options);
        spaceship = BitmapFactory.decodeResource(this.getResources(), R.drawable.spaceship, options);
        sliding1 = new Background(this, setting);
        sliding2 = new Background(this, setting);
        ship = new Rocket(this, spaceship, sliding1.SizeWidth(), sliding1.SizeHeight());
        sliding1.x2 = sliding1.SizeWidth();
        sliding1.y2 = sliding1.SizeHeight();
        sliding2.x1 = -sliding2.SizeWidth();
        sliding2.x2 = 10;
        sliding2.y2 = sliding2.SizeHeight();
        trj = new Trajectory(this);
        trj.createTrajectory();
        CFac = new CosmicFactory(trj, this);
        CFac.createCluster();
        //CFac.addCluster(1);
        dtpv = new DashTillPuffRenderThread(this);
        dtpv.start();
        //
        //
    }

    @Override
    public void surfaceChanged ( SurfaceHolder holder , int format , int width , int height ) {
        // Respond to surface changes , e . g . , aspect ratio changes .
    }

    @Override
    public void surfaceDestroyed ( SurfaceHolder holder ) {
        // The cleanest way to stop a thread is by interrupting it .
        // BubbleShooterThread regularly checks its interrupt flag .
        dtpv.interrupt() ;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                ship.ga = 1;
                ship.fVelY = ship.VelY/1.0 - 2;
                break;
            case MotionEvent.ACTION_UP:
                ship.ga = 0;
                ship.fVelY = ship.VelY/1.0;
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        //
        draw(c);
    }


    private void endCheck(){
        for (int i = 0; i < CFac.Clusters.size(); i++){
            //if (CFac.Clusters.get(i)[0].right < ship.x1){
                for(int j = 0; j < 10 ; j++){
                    if (CFac.Clusters.get(i)[j].contains(ship.x1+10, ship.y1+10) ||
                        CFac.Clusters.get(i)[j].contains(ship.x1+10, ship.y2-10) ||
                        CFac.Clusters.get(i)[j].contains(ship.x2-10, ship.y1+10) ||
                        CFac.Clusters.get(i)[j].contains(ship.x2-10, ship.y2-10)){ //if any of the four points is inside a cosmic object, start end game.
                            ship.lose = 1;
                    }
                }
            //}

        }
    }
    public void draw(Canvas c) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        c.drawPaint(paint); // Fill background
        sliding1.tick(c);
        sliding2.tick(c);
        trj.tick(c);
        ship.tick(c);
        CFac.tick(c);
        ship.hits(c, "Lives : " + ship.hits);
        if (ship.hits == 0){
            ship.lost(c, "YOU FAILED");
        }
        endCheck();
    }

    @Override
    public void tick(Canvas c){
        draw(c);
        //
        //
    }
}
