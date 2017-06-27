package com.eecs40.homework_2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.Random;

/**
  Created by Michael on 4/22/2015.
 */
public class Trajectory implements TimeConscious{
    public ArrayList<Point2D> points = new ArrayList<Point2D>();
    private float x;
    private float y;
    private int w;
    private int h;
    private final DashTillPuffSurfaceView dtpv;

    public Trajectory(DashTillPuffSurfaceView dtpv){
        this.dtpv = dtpv;
        this.x = 0;
        this.y = 0;
    }

    public void createTrajectory() {
        Random rng = new Random();
        w = dtpv.getWidth();
        h = dtpv.getHeight();
        this.x = - (7*w / 5);
        //creates initial points
        for (int i = 0; i < w; i++) {
            if (x > (w + 100)) {
                break;
            }
            y = rng.nextInt(((h - h/8) - h/8) + 1) + h/8;
            points.add(new Point2D(x, y));
            x += w / 5;
        }
    }

    private void draw(Canvas c){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        //draws line
        path.moveTo (points.get(0).x, points.get(0).y); // Move to first point
        for (int i = 1; i < points.size() ; ++ i ) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        //dashed line effect
        paint.setPathEffect(new DashPathEffect(new float[] {10, 10}, 5));
        c.drawPath (path, paint) ;
    }


    @Override
    public void tick(Canvas c) {
        Random rng = new Random();
        for(Point2D p:points){
            p.x += 3;
            //deletes last point when past the screen and adds a new point
            if(p.x > (w+200)){
                points.remove(p);
                y = rng.nextInt(((h - h/8) - h/8) + 1) + h/8;
                points.add(0, new Point2D(-7*w/5, y));
            }
        }
        draw(c);

        //System.out.println("Another line is being drawn");
    }
}
