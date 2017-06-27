package com.example.michael.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.Random;

/**
 * Created by Michael on 4/6/2015.
 * Edited around since 2015
 * Last Change: by Austin on 4/17/2015
 */
public class Bubble {
    private static int[] COLOR = {Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.GRAY,
                                    Color.YELLOW, Color.BLACK, Color.MAGENTA};

    private final BubbleShooterView bsv;
    public        int alpha;
    private       double dalpha;
    public        int mR;
    private       int mColor;
    public        double mVelX;
    public        double mVelY;
    public        int pX;
    public        int pY;
    private        int pX2;
    private        int pY2;
    public        int shoot;
    private       int pop;
    public       int gridX;
    public       int gridY;
    private      int tag;
    Random rng = new Random();

    //Constructor with default Bubble properties
    public Bubble(BubbleShooterView bsv){
        this.bsv = bsv;
        this.mColor = COLOR[rng.nextInt(COLOR.length)];
        this.alpha = 255;
        this.dalpha = 255.0;
        this.mR = 0;
        this.mVelX = 0;
        this.mVelY = 0;
        this.pX = 0;
        this.pY = 0;
        this.pX2 = 0;
        this.pY2 = 0;

        this.shoot = 0;
        this.pop = 0;
        this.gridX = 0;
        this.gridY = 0;
        this.tag = 0;
    }

    //Returns Width and Height
    public int SizeWidth(){
        return bsv.getWidth();
    }
    public int SizeHeight(){
        return bsv.getHeight();
    }

    //Draw circle

    public void draw(Canvas c){
        mR = (bsv.getWidth()/10)/2 - 1;
        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setAlpha(alpha);
        c.drawCircle(pX, pY, mR, paint);
        this.Move();
    }

    public void drawResults(Canvas c, String s){
        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(SizeWidth()/72*17);
        c.drawText(s, 10, SizeHeight()/2, paint);
        if(BubbleShooterView.loss == 2) {
            BubbleShooterView.rekt.mColor = COLOR[rng.nextInt(COLOR.length)];
        }
    }

    //Draw swipe/hit counter
    public void hits(Canvas c, String s){
        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(SizeWidth()/24);
        c.drawText(s, SizeWidth()*3/4, SizeHeight()*12/13, paint);
    }

    //Adapts final properties of the shooter ball and converts into a static bubble
    public void AddBall(){

        Random rng = new Random();
        BubbleShooterView.balls[BubbleShooterView.rekt.gridY][BubbleShooterView.rekt.gridX] = new Bubble(bsv);
        BubbleShooterView.balls[BubbleShooterView.rekt.gridY][BubbleShooterView.rekt.gridX].pX = BubbleShooterView.rekt.pX;
        BubbleShooterView.balls[BubbleShooterView.rekt.gridY][BubbleShooterView.rekt.gridX].pY = BubbleShooterView.rekt.pY;
        BubbleShooterView.balls[BubbleShooterView.rekt.gridY][BubbleShooterView.rekt.gridX].mColor = BubbleShooterView.rekt.mColor;
        BubbleShooterView.rekt.pX = SizeWidth() / 2;
        BubbleShooterView.rekt.pY = SizeHeight() - 100;
        BubbleShooterView.rekt.mColor = mColor = COLOR[rng.nextInt(COLOR.length)];
        BubbleShooterView.once = 0;
    }

    //Step movement for every frame. This is where checks and fade are called
    public void Move() {
        int maxX = SizeWidth();
        int maxY = SizeHeight();
        int brk = 0;

        int r = mR + 1;
        int d = r * 2;
        pX2 = pX;
        pY2 = pY;
        pX += mVelX;
        pY += mVelY;

        if (pX > (maxX - mR)) {
            mVelX = -mVelX;
            pX = maxX - mR;
        } else if (pX < mR) {
            mVelX = -mVelX;
            pX = mR;
        }
        if (pY > (maxY - mR)) {
            mVelY = -mVelY;
            pY = maxY - mR;
        } else if (pY < mR) {
            mVelY = -mVelY;
            pY = mR;
        }
        PopBalls();

        for (int i = 0; i < BubbleShooterView.Sizey1; i++) {
            for (int j = 0; j < BubbleShooterView.balls[i].length; j++) {
                if (BubbleShooterView.balls[i][j] == null || BubbleShooterView.balls[i][j].pop == 1) {
                    continue;
                }
                if ((BubbleShooterView.rekt.pX2 <= (BubbleShooterView.balls[i][j].pX2 + mR + mR / 2)) && (BubbleShooterView.rekt.pX2 >= (BubbleShooterView.balls[i][j].pX2 - mR - mR / 2))
                        && (BubbleShooterView.rekt.pY2 <= (BubbleShooterView.balls[i][j].pY2 + mR + mR / 2)) && (BubbleShooterView.rekt.pY2 >= (BubbleShooterView.balls[i][j].pY2 - mR - mR / 2))) {
                    mVelY = 0;
                    mVelX = 0;

                    //This is where we convert the float coordinate into grid coordinate for the array
                    gridY = (pY2/mR-1) / 2 + 1 ;

                    gridY = ((pY-mR*8/9)/mR-1) / 2 + 1 ;

                    pY = (2 * gridY + 1) * mR;
                    if (0 == gridY % 2) {
                        gridX = (pX2)/ d;
                        pX = r + d * gridX;
                    } else {
                        gridX = (pX2-mR) / d;
                        if (gridX == 9){
                            gridX--;
                        }
                        pX = (gridX + 1) * d;
                    }

                    AddBall();

                    //Tags initialized first so that Color check runs properly
                    TagWipe();
                    ColorCheck(gridY,gridX);

                    //Checks if top row has a ball and whether the player reached the bottom row
                    //This will end the game to the appropriate state and print win/lose messages
                    EndGameCheck(gridY);

                    //Now we break out of the double loop
                    brk = 1;
                    break;
                }
            }
            if (brk == 1) {
                break;
            }
        }
    }

    //Allows for recursive color check around a given ball at coordinate (Grid X, Grid Y)
    private void ColorCheck(int GY, int GX){
                        //To prevent stack over flow by marking balls from which recursion occurs
                        int count = 0;
                        BubbleShooterView.balls[GY][GX].tag = 1;
                        if(GY%2 == 0){ //If we're checking ball on an even row, check in clockwise as such
                            if  (0 != GY && 0 != GX && BubbleShooterView.balls[GY-1][GX-1] != null){ //top-left
                                if(BubbleShooterView.balls[GY-1][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;

                                    if(BubbleShooterView.balls[GY-1][GX-1].tag != 1){
                                      //  System.out.println("Here1 EVEN " + GY + " " + GX);
                                        ColorCheck(GY - 1, GX - 1);
                                    }
                                }
                            }
                            if  (0 != GY && null != BubbleShooterView.balls[GY-1][GX]){//top-right
                                if(BubbleShooterView.balls[GY-1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;

                                    if (BubbleShooterView.balls[GY-1][GX].tag != 1){
                                      //  System.out.println("Here2! EVEN "+ GY + " " + GX);
                                        ColorCheck(GY - 1, GX);

                                    }
                                }
                            }
                            if  (9 != GX && null != BubbleShooterView.balls[GY][GX+1]){ //level-right
                                if(BubbleShooterView.balls[GY][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;

                                    if(BubbleShooterView.balls[GY][GX+1].tag != 1){
                                      //  System.out.println("Here3! EVEN "+ GY + " " + GX);
                                        ColorCheck(GY, GX + 1);

                                    }
                                }
                            }
                            if  (null != BubbleShooterView.balls[GY+1][GX]){ //bottom-right
                                if(BubbleShooterView.balls[GY+1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;

                                    if(BubbleShooterView.balls[GY+1][GX].tag != 1){
                                     //   System.out.println("Here4! EVEN " + GY + " " + GX);
                                        ColorCheck(GY + 1, GX);

                                    }
                                }
                            }
                            if  (0 != GX && null != BubbleShooterView.balls[GY+1][GX-1]){ //bottom-left
                                if(BubbleShooterView.balls[GY+1][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;

                                    if(BubbleShooterView.balls[GY+1][GX-1].tag != 1){
                                    //   System.out.println("Here5! EVEN " + GY + " " + GX);
                                        ColorCheck(GY + 1, GX - 1);

                                    }
                                }
                            }
                            if  (0 != GX && null != BubbleShooterView.balls[GY][GX-1]){ //level-left
                                if(BubbleShooterView.balls[GY][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;

                                    if(BubbleShooterView.balls[GY][GX-1].tag != 1){
                                      //  System.out.println("Here6! EVEN " + GY + " " + GX);
                                        ColorCheck(GY, GX - 1);

                                    }
                                }
                            }

                        }
                        else{ //If we're checking ball on an odd row, check in clockwise as such
                            if  (null != BubbleShooterView.balls[GY-1][GX]){ //top-left
                                if(BubbleShooterView.balls[GY-1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;
                                    if(BubbleShooterView.balls[GY-1][GX].tag != 1){
                                      //  System.out.println("Here1 ODD " + GY + " " + GX);
                                        ColorCheck(GY - 1, GX);

                                    }
                                }
                            }
                            if  (null != BubbleShooterView.balls[GY-1][GX+1]){//top-right
                                if(BubbleShooterView.balls[GY-1][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;
                                    if(BubbleShooterView.balls[GY-1][GX+1].tag != 1){
                                     //  System.out.println("Here2! ODD "+ GY + " " + GX);
                                        ColorCheck(GY - 1, GX + 1);

                                    }
                                }
                            }
                            if  (8 != GX && null != BubbleShooterView.balls[GY][GX+1]){ //level-right
                                if(BubbleShooterView.balls[GY][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;
                                    if(BubbleShooterView.balls[GY][GX+1].tag != 1){
                                     //  System.out.println("Here3! ODD "+ GY + " " + GX);
                                        ColorCheck(GY, GX + 1);

                                    }
                                }
                            }
                            if  (null != BubbleShooterView.balls[GY+1][GX+1]){ //bottom-right
                                if(BubbleShooterView.balls[GY+1][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;
                                    if(BubbleShooterView.balls[GY+1][GX+1].tag != 1){
                                       // System.out.println("Here4! ODD "+ GY + " " + GX);
                                        ColorCheck(GY + 1, GX + 1);

                                    }
                                }
                            }
                            if  (null != BubbleShooterView.balls[GY+1][GX]){ //bottom-left
                                if(BubbleShooterView.balls[GY+1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;
                                    if(BubbleShooterView.balls[GY+1][GX].tag != 1){
                                       // System.out.println("Here5! ODD "+ GY + " " + GX);
                                        ColorCheck(GY + 1, GX);

                                    }
                                }
                            }
                            if  (0 != GX && null != BubbleShooterView.balls[GY][GX-1]){ //level-left
                                if(BubbleShooterView.balls[GY][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                    count++;
                                    if(BubbleShooterView.balls[GY][GX-1].tag != 1){
                                       // System.out.println("Here6! ODD "+ GY + " " + GX);
                                        ColorCheck(GY, GX - 1);

                                    }
                                }
                            }
                        }
                        //Now if a ball is surrounded by at least two others of the same color, we should mark them all
                      //  System.out.println("Counter of ball " + GY + " " + GX + " is " + count);
                        if (count > 1){
                           // System.out.println("ColorMarking from ball " + GY + " " + GX);
                            ColorMark(GY,GX);
                        }
                    }

    //A Supplement method to ColorCheck. Only marks surrounding balls for popping
    private void ColorMark(int GY, int GX) { //This is the function used to place pop marks

                    BubbleShooterView.balls[GY][GX].pop = 1;
                    if(GY%2 == 0){ //If we're checking ball on an even row, check in clockwise
                        if  (0 != GY && 0 != GX && null != BubbleShooterView.balls[GY-1][GX-1]){ //top-left
                            if(BubbleShooterView.balls[GY-1][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY-1][GX-1].pop = 1;
                            }
                        }
                        if  (0 != GY && null != BubbleShooterView.balls[GY-1][GX]){//top-right
                            if(BubbleShooterView.balls[GY-1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY-1][GX].pop = 1;
                            }
                        }
                        if  (9 != GX && null != BubbleShooterView.balls[GY][GX+1]){ //level-right
                            if(BubbleShooterView.balls[GY][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY][GX+1].pop = 1;
                            }
                        }
                        if  (null != BubbleShooterView.balls[GY+1][GX]){ //bottom-right
                            if(BubbleShooterView.balls[GY+1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY+1][GX].pop = 1;
                            }
                        }
                        if  (0 != GX && null != BubbleShooterView.balls[GY+1][GX-1]){ //bottom-left
                            if(BubbleShooterView.balls[GY+1][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY+1][GX-1].pop = 1;
                            }
                        }
                        if  (0 != GX && null != BubbleShooterView.balls[GY][GX-1]){ //level-left
                            if(BubbleShooterView.balls[GY][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY][GX-1].pop = 1;
                            }
                        }

                    }
                    else{ //If we're checking ball on an odd row, check in clockwise
                        if  (null != BubbleShooterView.balls[GY-1][GX]){ //top-left
                            if(BubbleShooterView.balls[GY-1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY-1][GX].pop = 1;
                            }
                        }
                        if  (null != BubbleShooterView.balls[GY-1][GX+1]){//top-right
                            if(BubbleShooterView.balls[GY-1][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY-1][GX+1].pop = 1;
                            }
                        }
                        if  (8 != GX && null != BubbleShooterView.balls[GY][GX+1]){ //level-right
                            if(BubbleShooterView.balls[GY][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY][GX+1].pop = 1;
                            }
                        }
                        if  (null != BubbleShooterView.balls[GY+1][GX+1]){ //bottom-right
                            if(BubbleShooterView.balls[GY+1][GX+1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY+1][GX+1].pop = 1;
                            }
                        }
                        if  (null != BubbleShooterView.balls[GY+1][GX]){ //bottom-left
                            if(BubbleShooterView.balls[GY+1][GX].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY+1][GX].pop = 1;
                            }
                        }
                        if  (0 != GX && null != BubbleShooterView.balls[GY][GX-1]){ //level-left
                            if(BubbleShooterView.balls[GY][GX-1].mColor == BubbleShooterView.balls[GY][GX].mColor) {
                                BubbleShooterView.balls[GY][GX-1].pop = 1;
                            }
                        }
                    }
    }

    //Cleans the tags used for Color Check. Note: Tags are used to prevent stack overflow
    private void TagWipe(){
        for(int i=0;i<BubbleShooterView.Sizey1;i++) {
            for (int j = 0; j < BubbleShooterView.balls[i].length; j++) {
                if (BubbleShooterView.balls[i][j] == null) {
                    continue;
                } else {
                    BubbleShooterView.balls[i][j].tag = 0;
                    //System.out.println("TagWiping i " + i + " j " + j);
                }
            }
        }
    }

    //Checks for balls with the pop flag on. This will cause a gradual fade first by adjusting alpha
    private void PopBalls(){
           double AlphaStep = 1.0/5;
           for(int i=0;i<BubbleShooterView.Sizey1;i++) {
                   for (int j = 0; j < BubbleShooterView.balls[i].length; j++) {
                       if (BubbleShooterView.balls[i][j] == null) {
                           continue;
                       }
                       if (BubbleShooterView.balls[i][j].pop == 1) {
                           BubbleShooterView.balls[i][j].dalpha -= AlphaStep;
                           BubbleShooterView.balls[i][j].alpha = (int) BubbleShooterView.balls[i][j].dalpha;
                       }
                       if (BubbleShooterView.balls[i][j].alpha <= 0) {
                           BubbleShooterView.balls[i][j] = null;
                       }
                   }
                   SupportCheck();

           }
    }

    //Checks every remaining ball whether it has support above it
    private void SupportCheck(){
            for(int i=1;i<BubbleShooterView.Sizey1;i++) {
                for (int j = 0; j < BubbleShooterView.balls[i].length; j++) {
                    if (BubbleShooterView.balls[i][j] == null || BubbleShooterView.balls[i][j].pop == 1 ) {
                        continue;
                    }
                    if(0 == i%2){//Even-rowed balls
                        if(j == 0){//Even & Touching Left wall
                            if((BubbleShooterView.balls[i-1][j] == null || BubbleShooterView.balls[i-1][j].pop == 1)){
                                BubbleShooterView.balls[i][j].pop = 1;
                                //System.out.println("Flag 1!");
                               // reset = 1;
                                continue;
                            }
                        }
                        if(j != 0 && j < 9){//Even & not Touching walls
                            if((BubbleShooterView.balls[i-1][j-1] == null || BubbleShooterView.balls[i-1][j-1].pop == 1)
                            && (BubbleShooterView.balls[i-1][j] == null || BubbleShooterView.balls[i-1][j].pop == 1)){
                                BubbleShooterView.balls[i][j].pop = 1;
                               // System.out.println("Flag 2!");
                               // reset = 1;
                                continue;
                            }
                        }
                        if(j == 9){//Even & Touching Right wall
                            if((BubbleShooterView.balls[i-1][j-1] == null || BubbleShooterView.balls[i-1][j-1].pop == 1)
                            && (BubbleShooterView.balls[i][j-1] == null || BubbleShooterView.balls[i][j-1].pop == 1)){
                                BubbleShooterView.balls[i][j].pop = 1;
                               // System.out.println("Flag 3!");
                               // reset = 1;
                                continue;
                            }
                        }
                    }
                    else{//Odd-rowed balls

                        if(j != 9
                        && (BubbleShooterView.balls[i-1][j] == null || BubbleShooterView.balls[i-1][j].pop == 1)
                        && (BubbleShooterView.balls[i-1][j+1] == null || BubbleShooterView.balls[i-1][j+1].pop == 1)){
                        BubbleShooterView.balls[i][j].pop = 1;
                            //reset = 1;
                           // System.out.println("Flag 5!");
                            continue;
                        }

                    }
                }
            }
    }

    //Here is where we check for end game flags. initiate win/lose statements
    private void EndGameCheck(int GY){
        if(GY == BubbleShooterView.Sizey1-2){
            //end the game
           // System.out.println("You Lost");
            BubbleShooterView.loss = 1;
            //BubbleShooterView.surfaceCreated (SurfaceHolder holder );
            //BubbleShooterView.bst.interrupt();

        }
        for (int i = 0; i < BubbleShooterView.balls[0].length; i++){
            if(BubbleShooterView.balls[0][i] == null || BubbleShooterView.balls[0][i].pop == 1){
                //System.out.println("Not-Continued" + i);
                if(i == 9){
                    //Winning the game
                    BubbleShooterView.loss = 2;
                   // System.out.println("Congratulations!!!");
                   // System.out.println("You have defeated Bubble Shooter in Proud Mode!!!");
                }
                continue;
            }
            if (BubbleShooterView.balls[0][i] != null || BubbleShooterView.balls[0][i].pop == 0) {
                //System.out.println("Continue" + i);
                break;
            }


        }
    }
}
