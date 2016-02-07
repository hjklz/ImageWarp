package com.imagewarp.andy.imagewarp;

import android.util.Log;
import android.view.ScaleGestureDetector;

//adapted from http://www.techotopia.com/index.php/Implementing_Android_Custom_Gesture_and_Pinch_Recognition#Detecting_Pinch_Gestures

public class TwoFingerGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

    private static final String DEBUG_TAG = "Pinch";
    private boolean warpHandled;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        if (warpHandled) {
            return true;
        }

        float scaleFactor = detector.getScaleFactor();

        if (scaleFactor > 1) {
            warpHandled = true;
            Log.d(DEBUG_TAG, "onScroll: HERE!");
//            scaleText.setText("Zooming Out");
        } else {
            warpHandled = true;
            Log.d(DEBUG_TAG, "onScroll: HERE2!");
//            scaleText.setText("Zooming In");
        }
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
