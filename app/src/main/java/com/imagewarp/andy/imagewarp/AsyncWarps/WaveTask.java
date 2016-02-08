package com.imagewarp.andy.imagewarp.AsyncWarps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.widget.ImageButton;

import com.imagewarp.andy.imagewarp.ScriptC_warps;

import java.nio.ByteBuffer;

public class WaveTask extends WarperTask{

    public WaveTask(Context context, ImageButton imageButton) {
        super(context, imageButton);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        //Just 1D array things
        Allocation tInAllocation = Allocation.createSized(myRs, Element.U8(myRs), refImage.getByteCount(), Allocation.USAGE_SCRIPT);

        //convert to byte array from http://stackoverflow.com/questions/10191871/converting-bitmap-to-bytearray-android
        ByteBuffer buffer = ByteBuffer.allocate(refImage.getByteCount()); //Create a new buffer
        refImage.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        byte[] imgByteArray = buffer.array();

        tInAllocation.copyFrom(imgByteArray);

        Allocation tOutAllocation = Allocation.createTyped(myRs, tInAllocation.getType());
        ScriptC_warps tScript = new ScriptC_warps(myRs);
        tScript.set_width(refImage.getWidth());
        tScript.set_height(refImage.getHeight());
        tScript.bind_input(tInAllocation);
        tScript.bind_output(tOutAllocation);
        tScript.invoke_wave();

        tOutAllocation.copyTo(imgByteArray);

        //http://stackoverflow.com/questions/18630080/android-byte-array-to-bitmap-how-to
        int nrOfPixels = imgByteArray.length / 4; // Four bytes per pixel.
        int pixels[] = new int[nrOfPixels];

        for(int i = 0; i < nrOfPixels; i++) {
            int r = imgByteArray[4*i] & 0xFF;
            int b = imgByteArray[4*i + 1] & 0xFF;
            int g = imgByteArray[4*i + 2] & 0xFF;
            int a = imgByteArray[4*i + 3] & 0xFF;

            pixels[i] = Color.argb(a, r, b, g);
        }
        newImage = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);

        return newImage;
    }
}
