package com.caoustc.test.wave;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;

import com.caoustc.wave.WaveView;
import com.caoustc.wave.WaveViewButton;

public class MainActivity extends AppCompatActivity {
    WaveViewButton waveViewButton;
    WaveView mWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waveViewButton = (WaveViewButton) findViewById(R.id.wave);
        mWaveView = (WaveView) findViewById(R.id.wave_view);
        waveViewButton.setDuration(2000);
        waveViewButton.setSpeed(600);
        waveViewButton.setCirclePadding(50);
        //waveViewButton.setCircleColor(R.color.colorAccent);
        //waveViewButton.setButtonColor(R.color.colorAccent);
        //waveViewButton.setColor(Color.parseColor("#25c4ff"));
        waveViewButton.setCircleStyle(Paint.Style.STROKE);
        waveViewButton.setInterpolator(new AccelerateInterpolator());
        waveViewButton.start();

        mWaveView.setDuration(5000);
        mWaveView.setStyle(Paint.Style.FILL);
        mWaveView.setColor(Color.RED);
        mWaveView.setInterpolator(new LinearOutSlowInInterpolator());
        mWaveView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waveViewButton != null) {
            waveViewButton.stop();
        }

        if (mWaveView != null) {
            mWaveView.stop();
        }
    }
}
