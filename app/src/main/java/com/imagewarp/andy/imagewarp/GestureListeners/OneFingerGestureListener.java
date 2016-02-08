package com.imagewarp.andy.imagewarp.GestureListeners;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.imagewarp.andy.imagewarp.AsyncWarps.SwirlTask;
import com.imagewarp.andy.imagewarp.Helpers.UndoStack;
import com.imagewarp.andy.imagewarp.AsyncWarps.WaveTask;

public class OneFingerGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final String DEBUG_TAG = "Scroll";

    private Context c;
    private ImageButton img;
    private UndoStack uStack;

    private boolean swipeHandled, swirlHandled;

    private float oldX;
    private float oldY;

    private boolean[] swirlcheck;

    public OneFingerGestureListener (Context c, ImageButton img, UndoStack uStack) {
        super();
        this.c = c;
        this.img = img;
        this.uStack = uStack;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        oldX = event.getX();
        oldY = event.getY();
        swirlcheck = new boolean[] {false, false, false, false}; //[-,+] ,[+, +] ,[+, -] ,[-, -]
        swipeHandled = false;
        swirlHandled = false;
//        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2,float distanceX, float distanceY) {
//        Log.d(DEBUG_TAG, "onScroll: " + event1.toString()+event2.toString());

        if (swipeHandled && swirlHandled) {
            return false;
        }

        //upswipe
        if (!swipeHandled) {
            if ((Math.abs(event1.getX() - event2.getX()) > 100)) {
                //            Log.d(DEBUG_TAG, "onScroll: HERE NO!");
                swipeHandled = true;
                return false;
            }

            if (((event2.getY() - event1.getY()) < -600)) {
//                Log.d(DEBUG_TAG, "onScroll: HERE!");
                uStack.push(((BitmapDrawable)img.getDrawable()).getBitmap());
                WaveTask wave = new WaveTask(c, img);
                wave.execute();
                swipeHandled = true;
                return true;
            }
        }

        //swirl
        //if scroll stays within a donut shape until it reaches start it will be similar to a swirl
        //i.e want to hit the following situations in this order
        //[oldX - curX, oldY - curY]
        //[-,+] ,[+, +] ,[+, -] ,[-, -]  -> cyclic list

        boolean isFirst = false;

        //first time
        if (oldX == event1.getX() && oldY == event1.getY()) {
            isFirst = true;
        }

        if ((oldX - event2.getX()) > 0) {
            if (oldY - event2.getY() > 0) {
                if (isFirst || swirlcheck[0] || swirlcheck[1]) {
                    swirlcheck[1] = true;
                } else {
                    swirlHandled = true;
                }
            } else {
                if (isFirst || swirlcheck[1] || swirlcheck[2]) {
                    swirlcheck[2] = true;
                } else {
                    swirlHandled = true;
                }
            }
        } else {
            if (oldY - event2.getY() > 0) {
                if (isFirst || swirlcheck[3] || swirlcheck [0]) {
                    swirlcheck[0] = true;
                } else {
                    swirlHandled = true;
                }
            } else {
                if (isFirst || swirlcheck[2] || swirlcheck[3]) {
                    swirlcheck[3] = true;
                } else {
                    swirlHandled = true;
                }
            }
        }

        boolean isSwirl = true;

        for (Boolean b: swirlcheck) {
            if(!b) {
                isSwirl = false;
            }
        }

//        Log.d(DEBUG_TAG, "Swirlcheck: " + swirlcheck[0] + " " + swirlcheck[1] + " " + swirlcheck[2] + " " + swirlcheck[3]);

        if (isSwirl) {
//            Log.d(DEBUG_TAG, "swirled");
            uStack.push(((BitmapDrawable) img.getDrawable()).getBitmap());
            SwirlTask swirl = new SwirlTask(c, img);
            swirl.execute();
            swirlHandled = true;
            return true;
        }

        oldX = event2.getX();
        oldY = event2.getY();

//        Log.d(DEBUG_TAG, (event2.getY() - event1.getY()) + "    " + (Math.abs(event1.getX() - event2.getX())));

        return false;
    }
}