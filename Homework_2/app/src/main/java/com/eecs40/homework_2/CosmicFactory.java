package com.eecs40.homework_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import java.util.Random;

/**
  Created by Michael on 4/29/2015.
 */
public class CosmicFactory {
    private int CObj[] = {R.drawable.blackhole, R.drawable.blueplanet, R.drawable.cloud,
            R.drawable.earth, R.drawable.glossayplanent, R.drawable.goldenstar,
            R.drawable.neutronstar, R.drawable.shinystar, R.drawable.star1, R.drawable.star2};
    private final Trajectory t;
    private final DashTillPuffSurfaceView dtpv;
    final Bitmap Cosbody;
    BitmapFactory.Options options = new BitmapFactory.Options();
    private static ArrayList<Bitmap> CosBodies = new ArrayList<Bitmap>();
    public static Rect Cluster[] = new Rect[10];
    public static ArrayList<Rect[]> Clusters = new ArrayList<Rect[]>();
    private int w;
    private int h;
    private Random rng = new Random();
    private static int down;
    public static int newbase;
    public static int newright;


    public CosmicFactory(Trajectory trj, DashTillPuffSurfaceView dtpv){
        this.t = trj;
        this.dtpv = dtpv;
        this.Cosbody = BitmapFactory.decodeResource(dtpv.getResources(), CObj[rng.nextInt(CObj.length)], options);
    }

    public void createCluster(){
        //Creating the first cluster based on point(5)
        int rpt = 7;
        w = dtpv.getWidth();
        h = dtpv.getHeight();
        //System.out.println("check y2 " + t.points.get(rpt).y + "check y1  " + t.points.get(rpt-1).y);
        double slope = (t.points.get(rpt).y - t.points.get(rpt-1).y)/(t.points.get(rpt).x - t.points.get(rpt-1).x);
        CosBodies.add(BitmapFactory.decodeResource(dtpv.getResources(), CObj[rng.nextInt(CObj.length)], options));
        Clusters.add(new Rect[10]);
        newbase = h - h/36;
        newright = (int)(t.points.get(rpt).x);
        //Self note Rect (Left , top , Right, Bottom)
        Clusters.get(Clusters.size()-1)[0] = new Rect ( newright - h/9, newbase - h/9, newright , newbase);
        for (int i = 1 ; i < 10; i++) {
            newbase -= h/6;
            //System.out.println("Slope " + slope);
            rpt = 0;
            while (t.points.get(rpt).x < newright){
                rpt++;
            }
            slope = (t.points.get(rpt).y - t.points.get(rpt-1).y)/(t.points.get(rpt).x - t.points.get(rpt-1).x);
            if (newbase - h / 4 < (t.points.get(rpt).y - slope * (t.points.get(rpt).x - Clusters.get(Clusters.size() - 1)[i - 1].right))
             && newbase - h / 4 < (t.points.get(rpt).y - slope * (t.points.get(rpt).x - Clusters.get(Clusters.size() - 1)[i - 1].left))) {
                newbase = h - rng.nextInt(5) * h / 72;
                newright = Clusters.get(Clusters.size() - 1)[i - 1].left - h / 12;
                //   System.out.println("Check1");
                rpt = 0;
                while (Clusters.get(Clusters.size() - 1)[i - 1].right > (int) (t.points.get(rpt).x)) {
                    rpt++;
                }
            }
            int xoffset = rng.nextInt(5) * h/72;
            Clusters.get(Clusters.size()-1)[i] = new Rect(newright - h/9 - xoffset, newbase - h/9, newright - xoffset, newbase);
        }
        //System.out.println("First cluster is created!");

    }


    public void addbotCluster(){
        //Creating the first cluster based on point(5)
        int rpt = 0;
        newright = Clusters.get(Clusters.size()-1)[9].left - h/4;
        while (t.points.get(rpt).x < newright){
            rpt++;
        }
        w = dtpv.getWidth();
        h = dtpv.getHeight();
        //System.out.println("check y2 " + t.points.get(rpt).y + "check y1  " + t.points.get(rpt-1).y);
        double slope = (t.points.get(rpt).y - t.points.get(rpt-1).y)/(t.points.get(rpt).x - t.points.get(rpt-1).x);
        CosBodies.add(BitmapFactory.decodeResource(dtpv.getResources(), CObj[rng.nextInt(CObj.length)], options));
        Clusters.add(new Rect[10]);
        newbase = h - h/36;

        //Self note Rect (Left , top , Right, Bottom)
        Clusters.get(Clusters.size()-1)[0] = new Rect ( newright - h/9, newbase - h/9, newright , newbase);
        for (int i = 1 ; i < 10; i++) {
            newbase -= h/6;
            //System.out.println("Slope " + slope);
            rpt = 0;
            while (t.points.get(rpt).x < newright){
                rpt++;
            }
            slope = (t.points.get(rpt).y - t.points.get(rpt-1).y)/(t.points.get(rpt).x - t.points.get(rpt-1).x);
            if (newbase - h/4 < (t.points.get(rpt).y - slope * ( t.points.get(rpt).x - Clusters.get(Clusters.size()-1)[i - 1].right))
             && newbase - h/4 < (t.points.get(rpt).y - slope * ( t.points.get(rpt).x - Clusters.get(Clusters.size()-1)[i - 1].left))) {
                newbase = h - rng.nextInt(5) * h/72;
                newright = Clusters.get(Clusters.size()-1)[i - 1].left - h/9;
                //   System.out.println("Check1");
                rpt = 0;
                while (Clusters.get(Clusters.size()-1)[i - 1].right > (int) (t.points.get(rpt).x)) {
                    rpt++;
                }
            }
            int xoffset = rng.nextInt(5) * h/72;
            Clusters.get(Clusters.size()-1)[i] = new Rect(newright - h/9 - xoffset, newbase - h/9, newright - xoffset, newbase);
        }
        //System.out.println("First cluster is created!");
    }

    public void addtopCluster(){
        //Creating the first cluster based on point(5)
        int rpt = 0;
        newright = Clusters.get(Clusters.size()-1)[9].left - h/4;
        while (t.points.get(rpt).x < newright){
            rpt++;
        }
        w = dtpv.getWidth();
        h = dtpv.getHeight();
        //  System.out.println("check y2 " + t.points.get(rpt).y + "check y1  " + t.points.get(rpt-1).y);
        double slope = (t.points.get(rpt).y - t.points.get(rpt-1).y)/(t.points.get(rpt).x - t.points.get(rpt-1).x);
        CosBodies.add(BitmapFactory.decodeResource(dtpv.getResources(), CObj[rng.nextInt(CObj.length)], options));
        Clusters.add(new Rect[10]);
        newbase =  h/9;
        //Self note Rect (Left , top , Right, Bottom)
        Clusters.get(Clusters.size()-1)[0] = new Rect ( newright - h/9, newbase - h/9, newright , newbase);
        for (int i = 1 ; i < 10; i++) {
            newbase += h/6;
            //    System.out.println("Slope " + slope);
            rpt = 0;
            while (t.points.get(rpt).x < newright) {
                rpt++;
            }
            slope = (t.points.get(rpt).y - t.points.get(rpt-1).y)/(t.points.get(rpt).x - t.points.get(rpt-1).x);
            if (newbase + h/8 + h/20 > (t.points.get(rpt).y - slope * ( t.points.get(rpt).x - Clusters.get(Clusters.size()-1)[i - 1].right))
             && newbase + h/8 + h/20> (t.points.get(rpt).y - slope * ( t.points.get(rpt).x - Clusters.get(Clusters.size()-1)[i - 1].left))) {
                newbase = rng.nextInt(5) * h/72 + h/9;
                newright = Clusters.get(Clusters.size()-1)[i - 1].left - h/9;
                //      System.out.println("Check1");
                rpt = 0;
                while (Clusters.get(Clusters.size()-1)[i - 1].right > (int) (t.points.get(rpt).x)) {
                    rpt++;
                }
            }
            int xoffset = rng.nextInt(5) * h/72;
            Clusters.get(Clusters.size()-1)[i] = new Rect(newright - h/9 - xoffset, newbase - h/9, newright - xoffset, newbase);
        }
        //System.out.println("First cluster is created!");

    }


    private void draw(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        for (int i = 0; i < Clusters.size(); i++){
            for(int j = 0; j < 10 ; j++){
                c.drawBitmap(CosBodies.get(i), null, Clusters.get(i)[j], paint);
            }
        }
    }
    private int count = 0;
    private int updown = 1;
    public void tick(Canvas c){
        count++;
        if(Clusters.get(Clusters.size()-1)[9].left > 0){
            if(updown == 1){
                addtopCluster();
                updown = 0;
            }
            else{
                addbotCluster();
                updown = 1;
            }
        }
        /*if (count == 150){

        }
        if (count == 300){

            count = 0;
        }*/
        draw(c);
        for (int i = 0; i < Clusters.size() ; i++){
            //.out.println(" " + i + "   " + Clusters.size());
            for (int j = 0; j < 10; j++){

                Clusters.get(i)[j].left += w/360;
                Clusters.get(i)[j].right += w/360;
            }
        }
        //System.out.println("" + Clusters.size());
       // System.out.println(Clusters.get(0)[9].left);
        if (Clusters.get(0)[9].left > dtpv.getWidth() ){
            Clusters.remove(0);
            CosBodies.remove(0);
        }
        //System.out.println("HELLO WORLD! " + Clusters.get(0)[9].left);
        //while (Clusters.get(Clusters.size()-1)[9].left > - 3){
          //  System.out.println("HELLO WORLD!");
          //addCluster();
        //}
    }
}
