package com.imagewarp.andy.imagewarp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

//adapted from yar's answer
//http://stackoverflow.com/questions/7887078/android-saving-file-to-external-storage
public class SaveTask extends AsyncTask<Void,Void,Void>{

    CharSequence s;
    Context context;
    ImageButton imageButton;
    Bitmap b;
    ProgressDialog progressDialog;

    SaveTask (Context context, ImageButton imageButton) {
        this.context = context;
        this.imageButton = imageButton;
        this.b = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();

        this.s = DateFormat.format("yyyyMMdd_HH_mm_ss", new Date().getTime());
    }

    @Override
    protected Void doInBackground(Void... params) {
        File myDir = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/iWARP");
        myDir.mkdirs();

        File file = new File (myDir, s.toString() + ".png");

        try {
            FileOutputStream out = new FileOutputStream(file);

            b.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        return null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Saving...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, "Saved as iWARP" + s, Toast.LENGTH_SHORT).show();
        progressDialog.hide();
        super.onPostExecute(aVoid);
    }
}