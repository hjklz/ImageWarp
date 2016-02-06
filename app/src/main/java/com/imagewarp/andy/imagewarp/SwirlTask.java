package com.imagewarp.andy.imagewarp;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.widget.ImageButton;

public class SwirlTask extends WarperTask {

    SwirlTask(Context context, ImageButton imageButton) {
        super(context, imageButton);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Allocation alloc =  Allocation.createFromBitmap(myRs, newImage);
        ScriptC_swirl swirl = new ScriptC_swirl(myRs);
        swirl.forEach_bar(alloc);
        alloc.copyTo(newImage);
        return newImage;
    }
}
