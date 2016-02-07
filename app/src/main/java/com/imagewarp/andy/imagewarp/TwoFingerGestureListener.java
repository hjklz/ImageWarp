package com.imagewarp.andy.imagewarp;

import android.content.Context;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.widget.ImageButton;

//adapted from http://www.techotopia.com/index.php/Implementing_Android_Custom_Gesture_and_Pinch_Recognition#Detecting_Pinch_Gestures

public class TwoFingerGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

    private static final String DEBUG_TAG = "Pinch";

    private Context c;
    private ImageButton img;

    private boolean warpHandled;

    public TwoFingerGestureListener (Context c, ImageButton img) {
        super();
        this.c = c;
        this.img = img;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        if (warpHandled) {
            return true;
        }

        BulgeTask bulge= new BulgeTask(c, img);
        bulge.execute();

        warpHandled = true;
//        Log.d(DEBUG_TAG, "onScroll: HERE2!");

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        warpHandled = false;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

}
