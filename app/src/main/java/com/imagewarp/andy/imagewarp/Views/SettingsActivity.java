package com.imagewarp.andy.imagewarp.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.imagewarp.andy.imagewarp.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private NumberPicker undoPicker;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        setUndoPicker(5);
        sharedPref = getSharedPreferences("ImageWarp", Context.MODE_PRIVATE);

        setResult(Activity.RESULT_CANCELED, new Intent());

        TextView curUndo = (TextView) findViewById(R.id.curUndo);

        //snippet 2
        curUndo.setText(Integer.toString(sharedPref.getInt(getString(R.string.undo), 1))); // grab value from shared preferences

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();

                //the number picker value is the index so need to reference the displayed values to get actual value
                editor.putInt(getString(R.string.undo), undoPicker.getValue());

                editor.commit();
                setResult(Activity.RESULT_OK, new Intent());
                finish();
            }
        });
    }

    private void setUndoPicker(int maxSize) {

        undoPicker = (NumberPicker) findViewById(R.id.undoPicker);
        undoPicker.setWrapSelectorWheel(false);

        // As per http://stackoverflow.com/questions/22370310/modifying-or-changing-min-max-and-displayed-values-for-numberpicker
        // making number picker ignore the length check of the values
        undoPicker.setMinValue(1);
        undoPicker.setMaxValue(maxSize);
    }

}
