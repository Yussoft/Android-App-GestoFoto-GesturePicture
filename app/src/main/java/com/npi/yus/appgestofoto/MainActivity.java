package com.npi.yus.appgestofoto;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*
    *Copyright (C) 2016  Jesús Sánchez de Castro
    *This program is free software: you can redistribute it and/or modify
    *it under the terms of the GNU General Public License as published by
    *the Free Software Foundation, either version 3 of the License, or
    *(at your option) any later version.
    *This program is distributed in the hope that it will be useful,
    *but WITHOUT ANY WARRANTY; without even the implied warranty of
    *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    *GNU General Public License for more details.
    *You should have received a copy of the GNU General Public License
    *along with this program.  If not, see <http://www.gnu.org/licenses/>
    *
    * @author Jesús Sánchez de Castro
    * @version 19.09.2016
    *
    Last Modification: 19/09/2016
    https://justyusblog.wordpress.com/
    https://github.com/Yussoft

    Bibliography:
    - https://developer.android.com/training/gestures/movement.html
    - https://developer.android.com/training/gestures/detector.html
    - https://developer.android.com/reference/android/view/View.html#onTouchEvent(android.view.MotionEvent)
    - http://stackoverflow.com/questions/17672891/getlocationonscreen-vs-getlocationinwindow
    - http://stackoverflow.com/questions/6745797/how-to-set-entire-application-in-portrait-mode-only
 */


/*
    MainActivity class:

    Implements everything related to the pattern/gesture and initiates the second Activity
    (Camera) when the pattern/gesture is done correctly.

 */
public class MainActivity extends AppCompatActivity {

    //Camera gesture/password
    private String gest_p = "";    //String modified everytime that a node is touched
    private String user_password;  //String set as password. gest_p and user_password must be
                                   //equal to start the camera.

    private boolean check_password = false;


    ImageView pictureImageView;
    protected Button saveGB, clearGB;

    //Nodes used to make a pattern (3x3)
    protected ImageView b11,b12,b13,b21,b22,b23,b31,b32,b33;

    //Array for (x,y) coordinates of the buttons
    int c11[] = new int[2];int c12[] = new int[2];int c13[] = new int[2];
    int c21[] = new int[2];int c22[] = new int[2];int c23[] = new int[2];
    int c31[] = new int[2];int c32[] = new int[2];int c33[] = new int[2];

    //Boolean variables to know if a button has been touched.
    private boolean t11,t12,t13,t21,t22,t23,t31,t32,t33;

    //Variables to store the raw coordinates (x,y) when touching the screen.
    private int xCoord, yCoord;

    //Debbug variables
    //private TextView xTextView, yTextView;

    /**********************************************************************************************/
    /*
        Method called when the Activity is created, initializes all the images used for the
        gesture, sets the screen orientation to portrait and sets 2 listeners for the buttons.

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Only portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //All the buttons have not been touched
        t11=t12=t13=t21=t22=t23=t31=t32=t33=false;

        b11 = (ImageView)findViewById(R.id.b11);
        b12 = (ImageView)findViewById(R.id.b12);
        b13 = (ImageView)findViewById(R.id.b13);
        b21 = (ImageView)findViewById(R.id.b21);
        b22 = (ImageView)findViewById(R.id.b22);
        b23 = (ImageView)findViewById(R.id.b23);
        b31 = (ImageView)findViewById(R.id.b31);
        b32 = (ImageView)findViewById(R.id.b32);
        b33 = (ImageView)findViewById(R.id.b33);

        //xTextView = (TextView)findViewById(R.id.coordX);
        //yTextView = (TextView)findViewById(R.id.coordY);

        //Button listener
        saveGB = (Button)findViewById(R.id.newGestureButton);
        saveGB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                saveGesture();
            }
        });

        clearGB = (Button)findViewById(R.id.clearGestureButton);
        clearGB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                clearGesture();
            }
        });
    }

    /**********************************************************************************************/
    /*
        Saves the current pattern/password in user_password and sets the nodes picture to off,
        booleans set to false and gest_p is empty.
     */
    protected void saveGesture(){
        user_password = gest_p;
        check_password = true;
        t11=t12=t13=t21=t22=t23=t31=t32=t33=false;
        b11.setImageResource(R.drawable.off);
        b12.setImageResource(R.drawable.off);
        b13.setImageResource(R.drawable.off);
        b21.setImageResource(R.drawable.off);
        b22.setImageResource(R.drawable.off);
        b23.setImageResource(R.drawable.off);
        b31.setImageResource(R.drawable.off);
        b32.setImageResource(R.drawable.off);
        b33.setImageResource(R.drawable.off);
        gest_p = "";
    }

    /**********************************************************************************************/
    /*
        Clear the current gesture, sets are the booleans to false, sets the off pictures and
        clears the gesture password String.
     */
    protected void clearGesture(){

        t11=t12=t13=t21=t22=t23=t31=t32=t33=false;
        b11.setImageResource(R.drawable.off);
        b12.setImageResource(R.drawable.off);
        b13.setImageResource(R.drawable.off);
        b21.setImageResource(R.drawable.off);
        b22.setImageResource(R.drawable.off);
        b23.setImageResource(R.drawable.off);
        b31.setImageResource(R.drawable.off);
        b32.setImageResource(R.drawable.off);
        b33.setImageResource(R.drawable.off);
        gest_p = "";
    }

    /**********************************************************************************************/
    /*
        Method activated when touching and moving the finger to make the pattern. Gets the (X,Y)
        coordinates and checks if the user is touching any of the nodes of the pattern, if so, the
        nodes turn green.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        //Get coordinates from the buttons
        b11.getLocationInWindow(c11);
        b12.getLocationInWindow(c12);
        b13.getLocationInWindow(c13);
        b21.getLocationInWindow(c21);
        b22.getLocationInWindow(c22);
        b23.getLocationInWindow(c23);
        b31.getLocationInWindow(c31);
        b32.getLocationInWindow(c32);
        b33.getLocationInWindow(c33);

        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                xCoord=(int)event.getRawX();
                yCoord=(int)event.getRawY();
                //xTextView.setText("("+String.valueOf(xCoord)+",");
                //yTextView.setText(String.valueOf(yCoord)+")");

                //If the node b11 is touched
                if(!t11 && xCoord >= c11[0] && xCoord <= (c11[0]+b11.getWidth()) &&
                        yCoord >= c11[1] && yCoord <= (c11[1]+b11.getHeight())){
                    b11.setImageResource(R.drawable.on);
                    t11 = true;
                    gest_p = gest_p + "1"; //Add the number of the node to the password
                }
                //If the node b12 is touched
                if(!t12 && xCoord >= c12[0] && xCoord <= (c12[0]+b12.getWidth()) &&
                        yCoord >= c12[1] && yCoord <= (c12[1]+b12.getHeight())){
                    b12.setImageResource(R.drawable.on);
                    t12 = true;
                    gest_p = gest_p + "2"; //Add the number of the node to the password
                }
                //If the node b13 is touched
                if(!t13 && xCoord >= c13[0] && xCoord <= (c13[0]+b13.getWidth()) &&
                        yCoord >= c13[1] && yCoord <= (c13[1]+b13.getHeight())){
                    b13.setImageResource(R.drawable.on);
                    t13 = true;
                    gest_p = gest_p + "3"; //Add the number of the node to the password
                }
                //If the node b21 is touched
                if(!t21 && xCoord >= c21[0] && xCoord <= (c21[0]+b21.getWidth()) &&
                        yCoord >= c21[1] && yCoord <= (c21[1]+b21.getHeight())){
                    b21.setImageResource(R.drawable.on);
                    t21 = true;
                    gest_p = gest_p + "4"; //Add the number of the node to the password
                }
                //If the node b22 is touched
                if(!t22 && xCoord >= c22[0] && xCoord <= (c22[0]+b22.getWidth()) &&
                        yCoord >=c22[1] && yCoord <= (c22[1]+b22.getHeight())){
                    b22.setImageResource(R.drawable.on);
                    t22 = true;
                    gest_p = gest_p + "5"; //Add the number of the node to the password
                }
                //If the node b23 is touched
                if(!t23 && xCoord >= c23[0] && xCoord <= (c23[0]+b23.getWidth()) &&
                        yCoord >= c23[1] && yCoord <= (c23[1]+b23.getHeight())){
                    b23.setImageResource(R.drawable.on);
                    t23 = true;
                    gest_p = gest_p + "6"; //Add the number of the node to the password
                }
                //If the node b31 is touched
                if(!t31 && xCoord >= c31[0] && xCoord <= (c31[0]+b31.getWidth()) &&
                        yCoord >= c31[1] && yCoord <= (c31[1]+b31.getHeight())){
                    b31.setImageResource(R.drawable.on);
                    t31 = true;
                    gest_p = gest_p + "7"; //Add the number of the node to the password
                }
                //If the node b32 is touched
                if(!t32 && xCoord >= c32[0] && xCoord <= (c32[0]+b32.getWidth()) &&
                        yCoord >= c32[1] && yCoord <= (c32[1]+b32.getHeight())){
                    b32.setImageResource(R.drawable.on);
                    t32 = true;
                    gest_p = gest_p + "8"; //Add the number of the node to the password
                }
                //If the node b33 is touched
                if(!t33 && xCoord >= c33[0] && xCoord <= (c33[0]+b33.getWidth()) &&
                        yCoord >= c33[1] && yCoord <= (c33[1]+b33.getHeight())){
                    b33.setImageResource(R.drawable.on);
                    t33 = true;
                    gest_p = gest_p + "9"; //Add the number of the node to the password
                }

            case MotionEvent.ACTION_UP:

                if(check_password){
                    if(user_password.equals(gest_p)){
                        System.out.println(gest_p);
                        System.out.println(user_password);
                        Intent intent = new Intent(this,CameraActivity.class);
                        startActivity(intent);
                    }
                    else if(gest_p.length()==9 && !user_password.equals(gest_p)){
                        clearGesture();
                    }
                }

            /*
            case MotionEvent.ACTION_DOWN:
                xCoord=(int)event.getRawX();
                yCoord=(int)event.getRawY();
                xTextView.setText("("+String.valueOf(xCoord)+",");
                yTextView.setText(String.valueOf(yCoord)+")");

            */
        }

        return true;
    }
    /**********************************************************************************************/
}
