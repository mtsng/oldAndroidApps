package com.example.michael.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.view.MotionEvent;

import java.lang.Math;

/**
 Created by Michael on 4/6/2015.
 */

// This is the ‘‘ game engine ’ ’.
public class BubbleShooterView extends SurfaceView implements SurfaceHolder.Callback {
        public static BubbleShooterThread bst;
        public static Bubble[][] balls;
        private Bubble initial = new Bubble(this); //reference object for methods
        public static Bubble rekt; //bubble object for shooting
        private int Sizex;
        public static int Sizey1; // the number of rows that will fill most of the screen
        private Arrow line;
        public static int cY; //row
        public static int dY; //difference in y distance between bubbles
        public static int dX; //difference in x distance between bubbles
        public static int hits; //displays number of swipes of shooting bubble
        public static int loss; //variable for end game state
        public static int once; //variable to prevent changing the balls trajectory mid-flight

    public BubbleShooterView ( Context context ) {
        super(context);
        // Notify the SurfaceHolder that you ’d like to receive
        // SurfaceHolder callbacks .
        getHolder().addCallback(this);
        setFocusable(true);
        // Initialize game state variables . DON ’T RENDER THE GAME YET .
        rekt = new Bubble(this);
        line = new Arrow(this);
        loss = 0;
    }

    @Override
    public void surfaceCreated ( SurfaceHolder holder ) {
    // Construct game initial state ( bubbles , etc .)
        Sizex = (initial.SizeWidth())/20;
        loss = 0;
        hits = 0;
        Sizey1 = initial.SizeHeight()/(Sizex*2);
        cY = Sizey1/2;

        //This is where we initialize the array
        balls = new Bubble[Sizey1][10];

        //initial array with bubbles
        for(int i = 0;i<Sizey1/2;i++){
                for (int j = 0; j < 10; j++) {
                   balls[i][j] = new Bubble(this);
                   balls[i][j].gridX = j;
                   balls[i][j].gridY = i;
                   if(i%2 == 0) {
                       balls[i][j].pX = Sizex + 2 * Sizex * j;
                   }
                   else{
                        balls[i][j].pX = 2 * Sizex + 2 * Sizex * j;
                   }
                   balls[i][j].pY = (i + 1) * (Sizex - 1) + (Sizex - 1) * i;

                }
        }

        dY = Math.abs(balls[0][0].pY - balls[1][0].pY);
        dX = Math.abs(balls[0][0].pX - balls[1][0].pX);

        //points offscreen bubble to null
        for(int i=0;i<Sizey1/2;i++){
            for(int j =0;j<10;j++){
                if(balls[i][j].pX == initial.SizeWidth()){
                    balls[i][j] = null;

                }
            }
        }

        //initialize the shooter ball position
        rekt.pX = initial.SizeWidth()/2;
        rekt.pY = initial.SizeHeight() - 100;

        //Set the line's coordinates
        //initiate shooting balls
        rekt.pX = initial.SizeWidth()/2;
        rekt.pY = initial.SizeHeight() - 100;

        //initiate arrow
        line.x1 = initial.SizeWidth()/2;
        line.y1 = initial.SizeHeight() - 100;
        line.x2 = line.x1;
        line.y2 = line.y1;

    // Launch animator thread .
        bst = new BubbleShooterThread (this) ;
        bst.start () ;
    }
    @Override
    public void surfaceChanged ( SurfaceHolder holder , int format , int width , int height ) {
    // Respond to surface changes , e . g . , aspect ratio changes .
    }
    @Override
    public void surfaceDestroyed ( SurfaceHolder holder ) {
    // The cleanest way to stop a thread is by interrupting it .
    // BubbleShooterThread regularly checks its interrupt flag .
        bst.interrupt () ;
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw (c) ;
        renderGame(c) ;
    }

    @Override
    public boolean onTouchEvent ( MotionEvent e ) {
    // Update game state in response to events :
    // touch - down , touch - up , and touch - move .
    // Current finger position .

        float sX = e.getX();
        float sY = e.getY();
        float fX = e.getX();
        float fY = e.getY();
        double Vx; //vector x
        double Vy; //vector y
        double uV; //unit vector

        switch ( e.getAction () ) {
            case MotionEvent.ACTION_DOWN :
                //checks if shooting balls was pressed
                if(sX <= (rekt.pX + 3*rekt.mR) && sX >= (rekt.pX - 3*rekt.mR) && sY >= (rekt.pY - 3*rekt.mR) && sY <= (rekt.pY + 3*rekt.mR)){
                    rekt.shoot = 1;
                    hits++; //This is where we increment the hit/swipe counter
                }
                // Update Game State .
                break ;
            case MotionEvent.ACTION_MOVE :
                //draws arrow
                if(rekt.shoot == 1 && once == 0) {
                    //offsets line for accuracy issues
                    line.x2 = fX - initial.SizeWidth()/120;
                    line.y2 = fY - initial.SizeWidth()/120;
                }
                // Update Game State .
                break ;
            case MotionEvent.ACTION_UP :
                line.x2 = line.x1;
                line.y2 = line.y1;
                //creates unit vector
                if(rekt.shoot == 1 && once == 0) {
                    Vx = fX - (initial.SizeWidth() / 2);
                    Vy = fY - (initial.SizeHeight() - 100);
                    uV = Math.sqrt((Vx * Vx) + (Vy * Vy));
                    rekt.mVelX = 20 * Vx * 1 / uV;
                    rekt.mVelY = 20 * Vy * 1 / uV;
                    rekt.shoot = 0;
                    once = 1;
                }
                // Update Game State .
                break ;
        }
        return true ;
    }
    public void advanceFrame ( Canvas c ) {
    // Update game state to animate moving or exploding bubbles
    // ( e . g . , advance location of moving bubble ) .
        renderGame (c) ;
    }
    private void renderGame (Canvas c) {
    // Render the game elements : bubbles ( fixed , moving , exploding )
    // and aiming arrow .
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        c.drawPaint(paint); // Fill background

       line.drawLine(c);

       rekt.draw(c);

       rekt.hits(c, "Swipes: "+ hits);

       for(int i=0;i<Sizey1;i++){
            for(int j=0;j<balls[i].length;j++){
                if(balls[i][j] == null){
                    continue;
                }
                balls[i][j].draw(c);
            }
       }

        //paints end game state results
        if (loss == 1){
            rekt.drawResults(c, "You Lost");
        }
        else if(loss == 2){
            rekt.drawResults(c, "You Won!");
        }
    }
}
