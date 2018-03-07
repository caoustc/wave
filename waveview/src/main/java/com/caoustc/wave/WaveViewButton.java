package com.caoustc.wave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 水波纹特效
 * Created by cz on 2017/8/14.
 */
public class WaveViewButton extends View implements WaveViewController {

    private final static int DEFAULT_TEXT_SIZE = 16; // sp

    private float mInitialRadius;   // 初始波纹半径
    private float mMaxRadius;       // 最大波纹半径
    private long mDuration = 1000;  // 一个波纹从创建到消失的持续时间
    private int mSpeed = 500;       // 波纹的创建速度，每500ms创建一个
    private float mMaxRadiusRate = 0.85f;
    private boolean mMaxRadiusSet;

    private boolean mIsRunning;
    private long mLastCreateTime;
    private List<Circle> mCircleList = new ArrayList<>();
    private float padding;
    private String createText;
    private float textSize;
    private int mTextColor;
    private int mButtonColor;
    private int mCircleColor;
    private int mBackupColor;
    private int mBackupColor2;

    private int mWaveTagType;

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    private Interpolator mInterpolator = new LinearInterpolator();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WaveViewButton(Context context) {
        this(context, null);
    }

    public WaveViewButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveViewButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveViewButton);
        createText = typedArray.getString(R.styleable.WaveViewButton_wave_text);
        padding = typedArray.getDimension(R.styleable.WaveViewButton_wave_circle_padding, dp2px(10));
        textSize = typedArray.getDimension(R.styleable.WaveViewButton_wave_text_size, sp2px(DEFAULT_TEXT_SIZE));
        mTextColor = typedArray.getColor(R.styleable.WaveViewButton_wave_text_color, Color.WHITE);
        mButtonColor = typedArray.getColor(R.styleable.WaveViewButton_wave_button_color, Color.GRAY);
        mCircleColor = typedArray.getColor(R.styleable.WaveViewButton_wave_circle_color, Color.GRAY);
        mBackupColor = typedArray.getColor(R.styleable.WaveViewButton_wave_button_color, Color.GRAY);
        mBackupColor2 = typedArray.getColor(R.styleable.WaveViewButton_wave_circle_color, Color.GRAY);
        typedArray.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCircleColor);
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!mMaxRadiusSet) {
            mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int DEFAULT = (int) dp2px(140);
        int width = measureDimension(DEFAULT, widthMeasureSpec);
        int height = measureDimension(DEFAULT, heightMeasureSpec);
        setMeasuredDimension(width, height);

        mInitialRadius = getWidth() / 2 - padding;
        mMaxRadius = mInitialRadius + padding / 2;
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(defaultSize, specSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                result = defaultSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setColor(mCircleColor);
                mPaint.setAlpha(circle.getAlpha());
                //mPaint.setColor(circleColor);
                //mPaint.setStrokeWidth(2);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
            } else {
                iterator.remove();
            }
        }

        drawCircleText(canvas);

        if (mCircleList.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    private void drawCircleText(Canvas canvas) {
        RectF oval = new RectF(padding, padding, getWidth() - padding, getHeight() - padding);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mButtonColor);
        canvas.drawOval(oval, paint);

        /*paint.setColor(mTextColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        Rect rect = new Rect();
        if (!TextUtils.isEmpty(createText)) {
            paint.getTextBounds(createText, 0, createText.length(), rect);
        }
        int h = rect.height();

        float baseline = oval.top + (oval.bottom - oval.top + h) / 2;
        float textX = oval.left + getWidth() / 2 - padding;
        canvas.drawText(createText, textX, baseline, paint);*/

        //float baseline = oval.top + (oval.bottom - oval.top + h) / 2;
        //float textX = oval.left + getWidth() / 2 - padding;
        //float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;

        if (!TextUtils.isEmpty(createText)) {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(mTextColor);
            textPaint.setTextSize(textSize);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setAntiAlias(true);
            StaticLayout layout = new StaticLayout(createText, textPaint, getWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            canvas.save();
            canvas.translate(getWidth() / 2, (getHeight() - layout.getHeight()) / 2);
            layout.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void setMaxRadiusRate(float maxRadiusRate) {
        mMaxRadiusRate = maxRadiusRate;
    }

    @Override
    public void setColor(int color) {
        if (mPaint != null) {
            mPaint.setColor(color);
        }
    }

    @Override
    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mCreateCircle.run();
        }
    }

    @Override
    public void stop() {
        mIsRunning = false;
    }

    @Override
    public void stopImmediately() {
        mIsRunning = false;
        mCircleList.clear();
        invalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setClickable(enabled);
        if (enabled) {
            mButtonColor = mBackupColor;
            mCircleColor = mBackupColor2;
        } else {
            mButtonColor = Color.parseColor("#e6e6e6");
            mCircleColor = Color.parseColor("#e6e6e6");
        }
    }

    @Override
    public void setInitialRadius(float radius) {
        mInitialRadius = radius;
    }

    @Override
    public void setDuration(long duration) {
        mDuration = duration;
    }

    @Override
    public void setMaxRadius(float maxRadius) {
        mMaxRadius = maxRadius;
        mMaxRadiusSet = true;
    }

    @Override
    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    @Override
    public void setTextColor(int textColor) {
        this.mTextColor = getContext().getResources().getColor(textColor);
    }

    @Override
    public void setButtonColor(int buttonColor) {
        this.mButtonColor = getContext().getResources().getColor(buttonColor);
        this.mBackupColor = getContext().getResources().getColor(buttonColor);
    }

    @Override
    public void setCircleColor(int circleColor) {
        this.mCircleColor = getContext().getResources().getColor(circleColor);
        this.mBackupColor2 = getContext().getResources().getColor(circleColor);
    }

    @Override
    public void setText(String createText) {
        this.createText = createText;
    }

    public void setCirclePadding(float padding) {
        this.padding = padding;
    }

    public int getWaveTagType() {
        return mWaveTagType;
    }

    public void setWaveTagType(int waveTagType) {
        mWaveTagType = waveTagType;
    }

    public void setCircleStyle(Paint.Style style) {
        if (mPaint != null) {
            mPaint.setStyle(style);
        }
    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
            return (int) (255 - mInterpolator.getInterpolation(percent) * 255) / (mPaint.getStyle() == Paint.Style.STROKE ? 1 : 2);
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }
}