package com.imagewarp.andy.imagewarp;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.util.Log;
import android.widget.ImageButton;

import java.nio.ByteBuffer;

public class SwirlTask extends WarperTask {

    SwirlTask(Context context, ImageButton imageButton) {
        super(context, imageButton);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
//        Allocation tInAllocation = Allocation.createFromBitmap(myRs, refImage, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        //Just 1D array things
        Allocation tInAllocation = Allocation.createSized(myRs, Element.U8(myRs), refImage.getByteCount(), Allocation.USAGE_SCRIPT);

        Log.d("render",Integer.toString(tInAllocation.getType().getX()));
        Log.d("render",Integer.toString(tInAllocation.getType().getY()));
        Log.d("render", Integer.toString(tInAllocation.getType().getZ()));

        //convert to byte array from http://stackoverflow.com/questions/10191871/converting-bitmap-to-bytearray-android
        ByteBuffer buffer = ByteBuffer.allocate(refImage.getByteCount()); //Create a new buffer
        refImage.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        Log.d("render2", Integer.toString(buffer.array().length));
        tInAllocation.copyFrom(buffer.array());

        Allocation tOutAllocation = Allocation.createTyped(myRs, tInAllocation.getType());
        ScriptC_swirl tScript = new ScriptC_swirl(myRs);
        tScript.set_width(newImage.getWidth());
        tScript.set_height(newImage.getHeight());
        tScript.bind_input(tInAllocation);
        tScript.bind_output(tOutAllocation);
        tScript.invoke_XXX(50,50);


//        Allocation alloc =  Allocation.createFromBitmap(myRs, newImage);
//        ScriptC_swirl swirl = new ScriptC_swirl(myRs);
//        swirl.forEach_bar(alloc);
        tOutAllocation.copyTo(newImage);
        return newImage;
    }
}
