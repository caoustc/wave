package com.caoustc.test.wave;

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
        waveViewButton.setSpeed(500);
        waveViewButton.setInterpolator(new AccelerateInterpolator());
        waveViewButton.start();
    }
}
