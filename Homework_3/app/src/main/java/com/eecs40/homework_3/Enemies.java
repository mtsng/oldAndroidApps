package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 Created by Michael on 5/12/2015.
 */
abstract public class Enemies implements TimeConscious{
    public int x;
    public int y;
    protected long GifStart;
    protected long now;
    protected Movie Enemy;
    protected MarioSurfaceView msv;
    private int relTime;
    private int dur;
    protected boolean collide;
    protected boolean isKilled;
    protected Bitmap death;
    protected int eH;
    protected int eW;
    public Rect enemyBox;
    private Rect dst2;
    BitmapFactory.Options options = new BitmapFactory.Options();

    protected void draw(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        //rect matches the gif position for accurate drawing of death state
        dst2 = new Rect(x, y + y/13, x + eW, y + eH);
        if(isKilled){
            //optimization via not draw off-screen objects
            if (x < 1600 && x > - 100){
                c.drawBitmap(death, null, dst2, paint);
            }
        }
        else {
            //initializes the gif
            now = android.os.SystemClock.uptimeMillis();
            if (GifStart == 0) {
                GifStart = now;
            }

            if (Enemy != null) {
                dur = Enemy.duration();
                if (dur == 0) {
                    dur = 1000;
                }
                relTime = (int) ((now - GifStart) % dur);
                Enemy.setTime(relTime);
                //optimization
                if (x < 1600 && x > -50){
                    Enemy.draw(c, x, y);
                }
                msv.invalidate();
            }
        }
    }

    //centerx
    public int Cx(){
        return Enemy.width()/2 + x;
    }

    //centery
    public int Cy(){
        return Enemy.height()/2 + y;
    }

    //right x side of gif
    public int rSide(){
        return Enemy.width() + x;
    }

    //bottom y of gif
    public int By(){ return Enemy.height() + y; }

    protected void testCollide(WorldCreator w){
        for(abstractBlock b:w.blocks) {
            if(b.blocktype != 1){
                continue;
            }
            int dx = this.Cx() - b.Cx();
            int dy = this.Cy() - b.Cy();
            double dist = Math.sqrt(dx*dx + dy*dy);
            //collision check for dark side of the block
            if(dist < (this.Cx() - x) + (b.Cx() - b.x1)){
                this.collide = true;
            }
            //collision check for light side of the block
            if(b.dst.contains(this.rSide(), this.Cy())){
                this.collide = false;
            }
        }

        //Rect for enemy collsion check in player
        enemyBox = new Rect(x, y, x + eW, y + eH);

        //Death by link's sword
        if (!isKilled && msv.link.isInvincible() && ((enemyBox.contains(msv.link.lSide(), msv.link.Cy()))
                                    || (enemyBox.contains(msv.link.lSide(), msv.link.y))
                                    || (enemyBox.contains(msv.link.rSide(), msv.link.Cy()))
                                    || (enemyBox.contains(msv.link.rSide(), msv.link.y))
                                    || (enemyBox.contains(msv.link.Cx(), msv.link.tSide()))
                                    || (enemyBox.contains(msv.link.Cx(), msv.link.bSide())))
                                        ){

                                        msv.addScore(100);
                                        isKilled = true;
        }

        //Death by link's foot
        else if (enemyBox.contains( msv.link.lSide() , msv.link.bSide() + 5)
              || enemyBox.contains (msv.link.rSide() , msv.link.bSide() + 5)
              ||(enemyBox.contains(msv.link.Cx(), msv.link.bSide() + 5))){
               // System.out.println("true dat");
                if(msv.link.falling && !isKilled ){
                   // System.out.println("KILL!!!");
                    msv.link.bounce();
                    msv.addScore(100);
                    isKilled = true;
                }

        }

    }

    public void tick(Canvas c){
        draw(c);
    }

}
