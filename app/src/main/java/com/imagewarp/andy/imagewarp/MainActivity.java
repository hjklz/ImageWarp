package com.imagewarp.andy.imagewarp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final int IMAGE_REQUEST = 57;
    public static final int CAM_REQUEST = 87;
    public static final int NO_IMAGE = 0;
    public static final int HAS_IMAGE = 1;

    private ImageButton imgButton;
    private Button saveButton;
    private Button undoButton;

    RenderScript myRs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRs = RenderScript.create(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        imgButton = (ImageButton) findViewById(R.id.Image);
        imgButton.setTag(NO_IMAGE);

        //TODO: TEMP SECTION START
        Button w1 = (Button) findViewById(R.id.w1);
        w1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SwirlTask swirl = new SwirlTask(getApplicationContext(), imgButton);
                swirl.execute();
            }
        });
        Button w2 = (Button) findViewById(R.id.w2);
        w2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        Button w3 = (Button) findViewById(R.id.w3);
        w3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        //TODO: TEMP SECTION END

        //could refactor these onClicks to use tags and a single listener
        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        undoButton = (Button) findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button loadButton = (Button) findViewById(R.id.Load);
        loadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onLoadImageClicked(v);
            }
        });

        Button takeButton = (Button) findViewById(R.id.New);
        takeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onNewImageClicked(v);
            }
        });

        saveButton.setEnabled(false);
        undoButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //convolution mask with no image doesn't make sense
            if ((Integer)imgButton.getTag() == NO_IMAGE) {
                Toast.makeText(this, "Load Image first!", Toast.LENGTH_LONG).show();
            } else {
                Intent settings = new Intent(this, SettingsActivity.class);
                //giving image parameters to settings
                //http://stackoverflow.com/questions/8880376/how-to-get-height-and-width-of-a-image-used-in-android
                //http://stackoverflow.com/questions/8306623/get-bitmap-attached-to-imageview

                Bitmap b = ((BitmapDrawable)imgButton.getDrawable()).getBitmap();

                //settings.putExtra("ImageSize", Math.min(b.getHeight(),b.getWidth()));
                startActivity(settings);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //the method will run then load image button is clicked
    public void onLoadImageClicked (View v) {
        //using snippet 5 to get images
        Intent imageIntent = new Intent(Intent.ACTION_PICK);

        File imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String imageDirPath = imageDir.getPath();
        Uri imageURI = Uri.parse(imageDirPath);

        imageIntent.setDataAndType(imageURI, "image/*");

        startActivityForResult(imageIntent, IMAGE_REQUEST);
    }

    //the method will run then Take A Photo button is clicked
    public void onNewImageClicked (View v) {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(camIntent, CAM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_REQUEST) {
                Uri imageURI = data.getData();

                try {
                    //image load success, change imagebutton and activate buttons
                    //reset convolution mask, cancel any existing filtering

                    InputStream inputStream = getContentResolver().openInputStream(imageURI);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    imgButton.setImageBitmap(image);
                    imgButton.setTag(HAS_IMAGE);

                    //SharedPreferences.Editor editor = getSharedPreferences("ImageFilter", Context.MODE_PRIVATE).edit();
                    //editor.putInt(getString(R.string.convuMask), 1);
                   //editor.commit();

                    //Toast.makeText(this, Integer.toString(getSharedPreferences("ImageFilter", Context.MODE_PRIVATE).getInt(getString(R.string.convuMask), 1)), Toast.LENGTH_LONG).show();

                    saveButton.setEnabled(true);
                    undoButton.setEnabled(true);

                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "Unable to load image", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            if (requestCode == CAM_REQUEST) {
                Bitmap pic = (Bitmap) data.getExtras().get("data");
                imgButton.setImageBitmap(pic);
            }
        }
    }
}