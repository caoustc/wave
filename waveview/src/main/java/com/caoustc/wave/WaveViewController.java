package com.caoustc.wave;

/**
 * Created by cz on 2017/9/22.
 */

public interface WaveViewController {

    public void setMaxRadiusRate(float maxRadiusRate);

    public void setColor(int color);

    public void start();

    public void stop();

    public void stopImmediately();

    public void setEnabled(boolean enabled);

    public void setInitialRadius(float radius);

    public void setDuration(long duration);

    public void setMaxRadius(float maxRadius);

    public void setSpeed(int speed);

    public void setTextColor(int textColor);

    public void setButtonColor(int buttonColor);

    public void setCircleColor(int circleColor);

    public void setText(String createText);

}
