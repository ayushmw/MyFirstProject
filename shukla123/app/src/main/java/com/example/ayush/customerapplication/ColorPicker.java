package com.example.ayush.customerapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class ColorPicker extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    Button bApplyCardColor;
    int red = 0, green = 0, blue = 0;
    View colorView;
    String cardColor = String.format("#%02x%02x%02x", red, green, blue).toUpperCase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);
        colorView = findViewById(R.id.colorView);
        colorView.setBackgroundColor(Color.rgb(red, green, blue));
        bApplyCardColor = (Button) findViewById(R.id.bApplyCardColor);
        bApplyCardColor.setOnClickListener(this);
        redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        switch (id){
            case R.id.redSeekBar:
                red = progress;
                break;
            case R.id.greenSeekBar:
                green = progress;
                break;
            case R.id.blueSeekBar:
                blue = progress;
                break;
        }
        colorView.setBackgroundColor(Color.rgb(red, green, blue));
        cardColor = String.format("#%02x%02x%02x", red, green, blue).toUpperCase();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("bgColor", cardColor);
        setResult(RESULT_OK, intent);
        finish();
    }
}
