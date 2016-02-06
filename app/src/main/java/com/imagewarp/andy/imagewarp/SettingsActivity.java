package com.imagewarp.andy.imagewarp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        sharedPref = getSharedPreferences("ImageFilter", Context.MODE_PRIVATE);

        TextView curConvu = (TextView) findViewById(R.id.curConvu);

        //snippet 2
        // curConvu.setText(Integer.toString(sharedPref.getInt(getString(R.string.convuMask), 1))); // grab value from shared preferences

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
                //editor.putInt(getString(R.string.convuMask), Integer.parseInt(convuPicker.getDisplayedValues()[convuPicker.getValue()-1]));

                editor.commit();
                finish();
            }
        });
    }

}
