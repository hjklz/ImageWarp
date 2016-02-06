package com.imagewarp.andy.imagewarp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.util.Log;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
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

        //convert to byte array from http://stackoverflow.com/questions/10191871/converting-bitmap-to-bytearray-android
        ByteBuffer buffer = ByteBuffer.allocate(refImage.getByteCount()); //Create a new buffer
        refImage.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        byte[] imgByteArray = buffer.array();

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        refImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] b = stream.toByteArray();



        Log.d("render",Integer.toString(tInAllocation.getType().getX()));
        Log.d("render",Integer.toString(tInAllocation.getType().getY()));
        Log.d("render", Integer.toString(tInAllocation.getType().getZ()));

        Log.d("render2", Integer.toString(imgByteArray.length));
        Log.d("render2", Integer.toString(imgByteArray[0]));
        Log.d("render2", Integer.toString(imgByteArray[1]));
        Log.d("render2", Integer.toString(imgByteArray[2]));
        Log.d("render2", Integer.toString(imgByteArray[3]));

        Log.d("render3", Integer.toString(refImage.getWidth()));
        Log.d("render3", Integer.toString(refImage.getHeight()));

        tInAllocation.copyFrom(imgByteArray);

        Allocation tOutAllocation = Allocation.createTyped(myRs, tInAllocation.getType());
        ScriptC_swirl tScript = new ScriptC_swirl(myRs);
        tScript.set_width(refImage.getWidth());
        tScript.set_height(refImage.getHeight());
        tScript.bind_input(tInAllocation);
        tScript.bind_output(tOutAllocation);
        tScript.invoke_XXX(50,50);

//        Allocation alloc =  Allocation.createFromBitmap(myRs, newImage);
//        ScriptC_swirl swirl = new ScriptC_swirl(myRs);
//        swirl.forEach_bar(alloc);

        tOutAllocation.copyTo(imgByteArray);

        //http://stackoverflow.com/questions/18630080/android-byte-array-to-bitmap-how-to
        int nrOfPixels = imgByteArray.length / 4; // Four bytes per pixel.
        int pixels[] = new int[nrOfPixels];
        //doesn't replicate exactly, not sure why
        for(int i = 0; i < nrOfPixels; i++) {
            int a = imgByteArray[4*i] & 0xFF;
            int r = imgByteArray[4*i + 1] & 0xFF;
            int g = imgByteArray[4*i + 2] & 0xFF;
            int b = imgByteArray[4*i + 3] & 0xFF;
            pixels[i] = Color.argb(a, r, g, b);
        }
        newImage = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);

        //newImage = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
        Log.d("render4", Integer.toString(newImage.getWidth()));
        Log.d("render4", Integer.toString(newImage.getHeight()));
        return newImage;
    }
}
