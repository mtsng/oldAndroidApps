package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.InputStream;

/**
  Created by Michael on 5/12/2015.
 */
public class Player implements TimeConscious{
    private MarioSurfaceView msv;
    private Bitmap playerright;
    private Bitmap playerleft;
    private Movie walkright;
    private InputStream spin;
    private Movie spinattack;
    private Movie walkleft;
    private WorldCreator w;
    public boolean moveright;
    public boolean moveleft;
    protected long GifStart;
    public int gifH;
    public int gifW;
    private int relTime;
    protected long now;
    private int dur;
    private int x1;
    private int y1;
    public int x2;
    public int y2;
    public int x;
    public int y;
    private int swordmode;
    private int speedmode;
    public boolean startJump = false;
    public boolean jump = false;
    public boolean goright = true;
    public boolean goleft;
    public boolean youdied;
    public boolean falling;
    public boolean rising;
    private double Vy;
    private int prevY;
    private int prevX;
    public boolean sShift;
    private int speed = 5;
    public static final double gravity = 3;


    public Player(MarioSurfaceView msv, Bitmap r, Bitmap l, Movie wR, Movie wL, WorldCreator w){
        this.msv = msv;
        this.w = w;
        this.playerright = r;
        this.playerleft = l;
        this.walkright = wR;
        this.walkleft = wL;
        this.gifH = wR.height();
        this.gifW = wR.width();
        this.Vy = 0;
        this.swordmode = 0;
        this.speedmode = 0;
        this.spin = msv._context.getResources().openRawResource(+R.drawable.spin);
        this.spinattack = Movie.decodeStream(spin);
    }

    public void setSword(int time){
        swordmode = time;
    }

    public boolean isInvincible(){
        return swordmode > 0;
    }

    public void setSpeed(int time){
        speedmode = time;
    }

    //Different speeds
    private void toggleSpeed(){
        if(speedmode > 0){
            speed = 15;
        }
        else{
            speed = 5;
        }
    }

    //Standard jump + general physics
    public void sJump(){
        //Initiate jump
        if (startJump) {
            jump = true;
            Vy = (speedmode > 0) ? -50 : -35;
            startJump = false;
        }

        //Change in vertical position
        prevY = y;
        y = y + (int)Vy ;

        //acceleration
        Vy = Vy + gravity;

        //terminal velocity
        Vy = (Vy > 50)? 50 : Vy;
    }
    private void land(){
        //allow jumping again & stop vertical movement
        jump = false;
        Vy = 0;
    }
    public void bounce(){
        Vy = -10;
    }


    //World Shift methods
    public boolean doShiftRight(){
        //Set left boundary for world shifting
        return ( x + gifW < msv.getWidth()/8 && moveleft);
    }
    public boolean doShiftLeft(){
        //Set right boundary for world shifting
        return ( x + gifW > 3*msv.getWidth()/8 && moveright);
    }
    public int shiftWorld(){
        return doShiftLeft()? getShift() : doShiftRight()? -getShift() : 0;
    }
    public int getShift(){
        return sShift? x - prevX : speed;
    }

    //Main link draw method
    private void draw(Canvas c){
        x1 = x;
        y1 = y;
        x2 = x + gifW;
        y2 = y + gifH;
        Paint paint = new Paint();
        now = android.os.SystemClock.uptimeMillis();
        if(GifStart == 0){
            GifStart = now;
        }
        //Set swordmode gif
        if(swordmode > 0){
            if(spinattack != null){
                dur = spinattack.duration();
                if(dur == 0){
                    dur = 1000;
                }
                relTime = (int)((now-GifStart)%dur);
                spinattack.setTime(relTime);
                spinattack.draw(c, x-15, y-10);
                msv.invalidate();
            }
        }
        else {
            //Set right gif
            if (moveright) {
                if (walkright != null) {
                    dur = walkright.duration();
                    if (dur == 0) {
                        dur = 1000;
                    }
                    relTime = (int) ((now - GifStart) % dur);
                    walkright.setTime(relTime);
                    //c.scale(.9f, .9f);
                    walkright.draw(c, x, y);
                    msv.invalidate();
                }
            //Set left gif
            } else if (moveleft) {
                if (walkleft != null) {
                    dur = walkleft.duration();
                    if (dur == 0) {
                        dur = 1000;
                    }
                    relTime = (int) ((now - GifStart) % dur);
                    walkleft.setTime(relTime);
                    walkleft.draw(c, x, y);
                    msv.invalidate();
                }
            //Set stationary bitmaps
            } else {
                Rect dst = new Rect(x1, y1, x2, y2);
                if (goright) {
                    c.drawBitmap(playerright, null, dst, paint);
                } else if (goleft) {
                    c.drawBitmap(playerleft, null, dst, paint);
                }
            }
        }
    }

    //Center (x,y)
    public int Cx(){
        return gifH/2 + x;
    }
    public int Cy(){
        return gifH/2 + y;
    }

    //up-down-left-right character boundaries
    public int rSide(){
        return gifW + x;
    }
    public int lSide(){
        return x;
    }
    public int tSide(){ return y; }
    public int bSide(){ return y + gifH;}

    //gif width-height
    public int gHeight(){
        return gifH;
    }
    public int gWidth(){
        return gifW;
    }


    //main player collide checks
    private void tbCheck(){
        for (abstractBlock b:w.blocks) {

            //Skip Unnecessary blocks to lessen contain check
            if (b == null || b.x1 > x + 2 * gifW || b.x2 < x - gifW) {
                //continue;
            }

            //Check if there is a block below
            else if (Vy > 0 && b.dst.contains(Cx(), bSide() + 2 /*- gifH/4*/)
                    //|| b.dst.contains(rSide() - gifW/4, bSide() - gifH/4)
                    || b.dst.contains(x + gifW / 4, bSide() + 2)
                    || b.dst.contains(x + 3 * gifW / 4, bSide() + 2)
                    ) {
                y = b.y1 - gifH;
                land();
            }

            //Check if there is a block above
            else if (Vy <= 0 && b.dst.contains(x + gifW / 4, y) || b.dst.contains(x + 3 * gifW / 4, y)) {
                y = b.y2;
                land();
                if (b instanceof qBlock && rising) {
                    ((qBlock) b).vomitItem(w);
                }
            }
        }
    }
    private void rlCheck(){
        for (abstractBlock b:w.blocks) {
            //Skip Unnecessary blocks to lessen contain check
            if (b == null || b.x1 > x + 2 * gifW || b.x2 < x - gifW) {
                //continue;
            }

            //Check if there is a block on left side
            else if (moveleft && (b.dst.contains( x, y + gifH/6 ) || b.dst.contains( x, y + 5*gifH/6 ))){
                x = b.x2;
                sShift = true;
            }

            //Check if there is a block on right side
            else if (moveright && (b.dst.contains(x + gifW, y + gifH/6) || b.dst.contains( x + gifW, y + 5*gifH/6 ))){
                x = b.x1 - gifW;
                sShift = true;
            }
        }
    }
    protected void heroCollide(WorldCreator w){
        //do top-bottom checks
        tbCheck();
        falling = (y - prevY) > 0;
        rising = (y - prevY) < 0;

        prevX = x;
        if (moveright && !doShiftLeft()) {
            x1 += speed;
            x2 += speed;
            x += speed;
        } else if (moveleft && x > 0 && !doShiftRight()) {
            x -= speed;
            x2 -= speed;
            x1 -= speed;
        }
        //do left-right checks
        rlCheck();

        //Check for enemies collisions
        for(Enemies e:w.enemies){
            if(e == null){
                continue;
            }
            else if(!e.isKilled && e.enemyBox != null && (e.enemyBox.contains(x, Cy()) || e.enemyBox.contains(rSide(), Cy())) && !isInvincible()){
                youdied = true;
                msv.removeHeart();
            }
        }
        if(y + gifH > msv.getHeight()){
            youdied = true;
            msv.removeHeart();
        }
        //check for items
        for(Items i:w.items){
            if(i == null){
                continue;
            }
            i.itemCollide();
        }
        for(Coins m:w.coins){
            if(m == null){
                continue;
            }
            if(m.coinCollide()){
                msv.addScore(m.points);
                w.coins.remove(m);
            }
        }
    }

    //Final drawing methods
    private void speedShadow(Canvas c){
        if (speedmode > 0){
            draw(c);
        }
    }
    public void tick(Canvas c){
        speedShadow(c);
        sJump();
        //power up counter
        toggleSpeed();
        if(swordmode > 0) {
            swordmode--;
        }
        if(speedmode > 0){
            speedmode--;
            System.out.println("speed "+speedmode);
        }
        heroCollide(w);
        draw(c);
    }
}
