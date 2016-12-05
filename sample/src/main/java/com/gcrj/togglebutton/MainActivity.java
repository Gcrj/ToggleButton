package com.gcrj.togglebutton;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gcrj.togglebuttonlibrary.OnCheckedChangeListener;
import com.gcrj.togglebuttonlibrary.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.tb);
        toggleButton.setChecked(false, false);
//                toggleButton.toggle(false);
        toggleButton.setAnimDuration(200);
        toggleButton.setStrokeWidth(2);
        toggleButton.setCircleColor(Color.YELLOW);
        toggleButton.setBackCheckedColor(Color.rgb(200, 200, 0));
        toggleButton.setBackUncheckedColor(Color.GRAY);

        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ToggleButton toggleButton, boolean isChecked) {
                Toast.makeText(MainActivity.this, isChecked + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
