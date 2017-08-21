package com.caoustc.test.wave;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;

import com.caoustc.wave.WaveViewButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WaveViewButton waveViewButton = (WaveViewButton) findViewById(R.id.wave);
        waveViewButton.setDuration(2000);
        waveViewButton.setStyle(Paint.Style.STROKE);
        waveViewButton.setSpeed(1000);
        waveViewButton.setInterpolator(new AccelerateInterpolator());
        waveViewButton.start();
    }
}
