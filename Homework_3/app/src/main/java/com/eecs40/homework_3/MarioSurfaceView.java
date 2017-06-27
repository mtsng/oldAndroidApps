package com.eecs40.homework_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import java.io.InputStream;
import java.util.ArrayList;

/**
 Created by Michael on 5/11/2015.
 */
public class MarioSurfaceView extends SurfaceView implements SurfaceHolder.Callback, TimeConscious {
    private MarioThread mt;
    public static Bitmap setting;
    public static Bitmap Rplayer;
    public static Bitmap Lplayer;
    private static Bitmap jumpbutton;
    private static Bitmap leftbutton;
    private static Bitmap rightbutton;
    private InputStream wLeft;
    private InputStream wRight;
    private Movie wR;
    private Movie wL;
    public Player link;
    private Background bkg;
    public WorldCreator w1;
    public WorldCreator w2;
    public WorldCreator w3;
    private static int topY;
    public Context _context;
    private MarioMenu menu;
    private Tutorial tutorial;
    private boolean showtutorial;
    private boolean showMenu = true;
    private boolean stage1 = true;
    private boolean stage2 = false;
    private boolean stage3 = false;
    private boolean gameover;
    private int numberscore;
    private String stringscore = "Score: 0";
    private ArrayList<Lives> hearts = new ArrayList<>();
    BitmapFactory.Options options = new BitmapFactory.Options();
    private SparseArray<PointF> mActivePointers;


    public MarioSurfaceView(Context context){
        super(context);
        getHolder().addCallback(this);
        this._context = context;
        //decode gifs
        wLeft = context.getResources().openRawResource(+R.drawable.walkleft);
        wRight = context.getResources().openRawResource(+R.drawable.walkright);
        mActivePointers = new SparseArray<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        setting = BitmapFactory.decodeResource(this.getResources(), R.drawable.background, options);
        Rplayer = BitmapFactory.decodeResource(this.getResources(), R.drawable.idleright, options);
        Lplayer = BitmapFactory.decodeResource(this.getResources(), R.drawable.idleleft, options);
        jumpbutton = BitmapFactory.decodeResource(this.getResources(), R.drawable.upbutton, options);
        leftbutton = BitmapFactory.decodeResource(this.getResources(), R.drawable.leftbutton, options);
        rightbutton = BitmapFactory.decodeResource(this.getResources(), R.drawable.rightbutton, options);

        addHearts(5);
        wR = Movie.decodeStream(wRight);
        wL = Movie.decodeStream(wLeft);
        menu = new MarioMenu(this, options);
        tutorial = new Tutorial(this, options);
        w1 = new World1(this, _context);
        w2 = new World2(this, _context);
        w3 = new World3(this, _context);
        w1.InitializeWorld();
        w2.InitializeWorld();
        w3.InitializeWorld();
        topY = w1.base;
        bkg = new Background(this, setting);
        link = new Player(this, Rplayer, Lplayer, wR, wL, w1);
        link.x = (this.getWidth() - 800) - wR.width();
        link.y = topY - wR.height();

        bkg.x2 = bkg.SizeWidth();
        bkg.y2 = bkg.SizeHeight();

        mt = new MarioThread(this);
        mt.start();

    }

    //allows progress to next stage
    public void worldEnd(int w){
        if(w == 1){
            addScore(200);
            w2 = new World2(this, _context);
            w2.InitializeWorld();
            w1 = null;
            w3 = null;
            stage1 = false;
            stage2 = true;
            stage3 = false;
            link = new Player(this, Rplayer, Lplayer, wR, wL, w2);
            link.x = (this.getWidth() - 800) - wR.width();
            link.y = topY - wR.height();
        }
        else if(w == 2){
            addScore(200);
            w3 = new World3(this, _context);
            w3.InitializeWorld();
            w2 = null;
            w1 = null;
            stage1 = false;
            stage2 = false;
            stage3 = true;
            link = new Player(this, Rplayer, Lplayer, wR, wL, w3);
            link.x = (this.getWidth() - 800) - wR.width();
            link.y = topY - wR.height();
        }
        else if(w == 3){
            addScore(200);
            w1 = new World1(this, _context);
            w1.InitializeWorld();
            w2 = null;
            w3 = null;
            stage1 = true;
            stage2 = false;
            stage3 = false;
            link = new Player(this, Rplayer, Lplayer, wR, wL, w1);
            link.x = (this.getWidth() - 800) - wR.width();
            link.y = topY - wR.height();
        }
    }

    public void addScore(int p){
        numberscore += p;
        stringscore = "Score: "+Integer.toString(numberscore);
    }

    //restart world when one heart is lost
    public void restartWorld(int w){
        if(w == 1){
            w1 = new World1(this, _context);
            w1.InitializeWorld();
            stage1 = true;
            stage2 = false;
            stage3 = false;
            link = new Player(this, Rplayer, Lplayer, wR, wL, w1);
            link.x = (this.getWidth() - 800) - wR.width();
            link.y = topY - wR.height();
        }
        else if(w == 2){
            w2 = new World2(this, _context);
            w2.InitializeWorld();
            stage1 = false;
            stage2 = true;
            stage3 = false;
            link = new Player(this, Rplayer, Lplayer, wR, wL, w2);
            link.x = (this.getWidth() - 800) - wR.width();
            link.y = topY - wR.height();
        }
        else if(w == 3){
            w3 = new World3(this, _context);
            w3.InitializeWorld();
            stage1 = false;
            stage2 = false;
            stage3  = true;
            link = new Player(this, Rplayer, Lplayer, wR, wL, w3);
            link.x = (this.getWidth() - 800) - wR.width();
            link.y = topY - wR.height();
        }
    }

    //life points
    private void addHearts(int n){
        int x1 = 250;
        int x2 = 290;
        int dx = x2 - x1;
        for(int i=0;i<n;i++){
            hearts.add(new Lives(this, x1 + i*dx, 10, x2 + i*dx, 45, options));
        }
    }

    public void removeHeart(){
        if(hearts.size() != 0){
            hearts.remove(hearts.size()-1);
        }
        else{
            //no more hearts = game over
            gameover = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        mt.interrupt();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        int pointerIndex = e.getActionIndex();
        int pointerID = e.getPointerId(pointerIndex);
        float x1 = e.getX(pointerIndex);
        float y1 = e.getY(pointerIndex);

        if(showMenu){
            switch(e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //exits to game when start button is pressed
                    if(x1 > menu.SizeWidth()/2 - 100 && y1 > menu.SizeHeight() - 200 && x1 < menu.SizeWidth()/2 + 100 && y1 < menu.SizeHeight() - 15){
                        showMenu = false;
                        showtutorial = false;
                    }
                    //exits to tutorial screen
                    else if(x1 > menu.SizeWidth()/2 + 200 && y1 > menu.SizeHeight() -  150 && x1 <  menu.SizeWidth()/2 + 400 && y1 < menu.SizeHeight() - 50){
                        showtutorial = true;
                        showMenu = false;
                    }
                    break;
            }
            return true;
        }
        else if(showtutorial){
            switch(e.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //exits backt to start screen
                    if(x1 > 0 && y1 > tutorial.SizeHeight() - 100 && x1 < tutorial.SizeWidth()/10 && y1 < tutorial.SizeHeight()){
                        showtutorial = false;
                        showMenu = true;
                    }
                    break;
            }
            return true;
        }
        else {
            switch (e.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                //movement left - left button pressed
                if(x1 > 10 && y1 > topY && x1 < 110 && y1 < this.getHeight()){
                    link.moveleft = true;
                    link.goleft = true;
                    link.goright = false;
                    //System.out.println("left");
                }
                //movement right - right button pressed
                else if(x1 > 130 && y1 > topY && x1 < 230 && y1 < this.getHeight()){
                    //System.out.println("right");
                    link.moveright = true;
                    link.goright = true;
                    link.goleft = false;
                }
                //jump - jump button pressed
                else if(x1 > (this.getWidth()-170) && y1 > topY && x1 < (this.getWidth()-70) && y1 < this.getHeight()){
                    //System.out.println("up");
                    if (!link.jump && !link.falling){
                       // System.out.println("jump!");
                        link.startJump = true;
                    }
                }

                    //System.out.println("1");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    //System.out.println("2 ");
                    //multi-touch support
                    PointF f = new PointF();
                    f.x = e.getX(pointerIndex);
                    f.y = e.getY(pointerIndex);
                    mActivePointers.put(pointerID, f);
                    //cycle through pointers to find positions
                    for (int i = 0; i < e.getPointerCount(); i++) {
                        PointF p = mActivePointers.get(e.getPointerId(i));
                        if (p == null) {
                            continue;
                        }
                        //movement left
                        if (p.x > 10 && p.y > (this.getHeight() - 100) && p.x < 110 && p.y < this.getHeight()) {
                            link.moveleft = true;
                            link.goleft = true;
                            link.goright = false;
                        }
                        //movement right
                        else if (p.x > 130 && p.y > (this.getHeight() - 100) && p.x < 230 && p.y < this.getHeight()) {
                            link.moveright = true;
                            link.goright = true;
                            link.goleft = false;
                        }
                        //jump
                        if(x1 > (this.getWidth()-170) && y1 > (this.getHeight()-100) && x1 < (this.getWidth()-70) && y1 < this.getHeight()){
                            if (!link.jump && !link.falling){
                                link.startJump = true;
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mActivePointers.remove(pointerID);

                    link.moveright = false;
                    link.moveleft = false;

                    break;
                case MotionEvent.ACTION_POINTER_UP:

                    break;
            }
        }
        return true;
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);

        draw(c);
    }

    public void draw(Canvas c){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        c.drawPaint(paint);
        paint.setAntiAlias(true);

        if(showMenu){
            menu.tick(c);
        }
        else if(showtutorial){
            tutorial.tick(c);
        }
        else {
            bkg.tick(c);
            //draw score
            paint.setTextSize(this.getWidth() / 40);
            c.drawText(stringscore, this.getWidth() / 128, this.getHeight() / 16, paint);
            //draw hearts
            for(Lives h:hearts) {
                if (h == null) {
                    continue;
                }
                h.draw(c);
            }
            if(stage1) {
                w1.tick(c);
            }
            else if(stage2){
                w2.tick(c);
            }
            else if(stage3){
                w3.tick(c);
            }

            //draws buttons
            Rect dst1 = new Rect(10, topY, 110, this.getHeight());
            Rect dst2 = new Rect(130, topY, 230, this.getHeight());
            Rect dst3 = new Rect(this.getWidth() - 170, topY, this.getWidth() - 70, this.getHeight());
            c.drawBitmap(leftbutton, null, dst1, paint);
            c.drawBitmap(rightbutton, null, dst2, paint);
            c.drawBitmap(jumpbutton, null, dst3, paint);

            link.tick(c);
        }
        //restart everything upon total loss - boots to start screen
        if(gameover){
            gameover = false;
            showMenu = true;
            addHearts(5);
            numberscore = 0;
            stringscore = "Score: 0";
            restartWorld(1);
            try{
                Thread.sleep(2000);
            }
            catch(InterruptedException ie){

            }
        }
    }

    @Override
    public void tick(Canvas c){
        draw(c);

    }

}
