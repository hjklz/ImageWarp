package com.imagewarp.andy.imagewarp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.renderscript.RenderScript;
import android.widget.ImageButton;

abstract class WarperTask extends AsyncTask<Void,Integer,Bitmap> {

    Context context;
    ImageButton imageButton;

    Bitmap refImage, newImage;
    int width, height, totalPixels;

    ProgressDialog progressDialog;

    RenderScript myRs;

    WarperTask (Context context, ImageButton imageButton) {
        this.context = context;
        this.imageButton = imageButton;

        this.refImage = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
        this.width = refImage.getWidth();
        this.height = refImage.getHeight();
        this.totalPixels = width*height;

        //this.newImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.myRs = RenderScript.create(context);
    }

    @Override
    abstract protected Bitmap doInBackground(Void... params);

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Filtering...");
        progressDialog.setMax(width);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageButton.setImageBitmap(result);
        progressDialog.hide();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        progressDialog.setProgress(progress);
    }
}
