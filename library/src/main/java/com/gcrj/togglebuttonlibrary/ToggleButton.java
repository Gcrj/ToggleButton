package com.gcrj.togglebuttonlibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhangxin on 2016-11-28.
 */

public class ToggleButton extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private boolean checked;
    private int animDuration;
    private int strokeWidth;
    private int circleColor;
    private int backUncheckedColor;
    private int backCheckedColor;
    private OnCheckedChangeListener onCheckedChangeListener;

    private Paint circlePaint;
    private Paint backPaint;
    private Path linePath;
    private Path circlePath;
    private ValueAnimator valueAnimator;

    private boolean running;
    private int width;
    private int height;

    public ToggleButton(Context context) {
        super(context);
        animDuration = 200;
        strokeWidth = 2;
        circleColor = Color.YELLOW;
        backUncheckedColor = Color.GRAY;
        backCheckedColor = Color.rgb(200, 200, 0);
        init();
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToggleButton);
        checked = ta.getBoolean(R.styleable.ToggleButton_checked, false);
        animDuration = ta.getInt(R.styleable.ToggleButton_anim_duration, 200);
        strokeWidth = (int) ta.getDimension(R.styleable.ToggleButton_stroke_width, 2f);
        circleColor = ta.getColor(R.styleable.ToggleButton_circle_color, Color.YELLOW);
        backUncheckedColor = ta.getColor(R.styleable.ToggleButton_back_unchecked_color, Color.GRAY);
        backCheckedColor = ta.getColor(R.styleable.ToggleButton_back_checked_color, Color.rgb(200, 200, 0));
        ta.recycle();
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(strokeWidth);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleColor);
        backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setStrokeWidth(strokeWidth);
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(backUncheckedColor);

        circlePath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                width = 110;
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = 60;
                break;
        }
        setMeasuredDimension(width, height);

        resetPath();
    }

    private void resetPath() {
        if (linePath == null) {
            linePath = new Path();
        } else {
            linePath.reset();
        }
        linePath.addArc(new RectF(strokeWidth, strokeWidth, height - strokeWidth, height - strokeWidth), 90, 180);
        linePath.lineTo(width - height / 2, strokeWidth);
        linePath.addArc(new RectF(width - height, strokeWidth, width - strokeWidth, height - strokeWidth), -90, 180);
        linePath.lineTo(height / 2, height - strokeWidth);
        circlePath.reset();
        if (checked) {
            circlePath.addCircle(width - height / 2, height / 2, height / 2 - strokeWidth * 2, Path.Direction.CW);
            backPaint.setColor(backCheckedColor);
        } else {
            circlePath.addCircle(height / 2, height / 2, height / 2 - strokeWidth * 2, Path.Direction.CW);
            backPaint.setColor(backUncheckedColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (checked) {
            backPaint.setColor(backCheckedColor);
        } else {
            backPaint.setColor(backUncheckedColor);
        }
        canvas.drawPath(linePath, backPaint);
        canvas.drawPath(circlePath, circlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (!running) {
                    toggle(true);
                }
                break;
        }
        return true;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();

        Bundle bundle = new Bundle();
        bundle.putBoolean("checked", checked);
        bundle.putInt("animDuration", animDuration);
        bundle.putInt("strokeWidth", strokeWidth);
        bundle.putInt("circleColor", circleColor);
        bundle.putInt("backUncheckedColor", backUncheckedColor);
        bundle.putInt("backCheckedColor", backCheckedColor);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            checked = bundle.getBoolean("checked", false);
            animDuration = bundle.getInt("animDuration", 200);
            strokeWidth = bundle.getInt("strokeWidth", 2);
            circleColor = bundle.getInt("circleColor", Color.YELLOW);
            backUncheckedColor = bundle.getInt("backUncheckedColor", Color.GRAY);
            backCheckedColor = bundle.getInt("backCheckedColor", Color.rgb(200, 200, 0));
        }
    }

    public void setChecked(boolean checked, boolean anim) {
        if (this.checked != checked) {
            toggle(anim);
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void toggle(boolean anim) {
        if (running) {
            return;
        }

        if (anim) {
            running = true;

            if (checked) {
                valueAnimator = ValueAnimator.ofInt(width - height / 2, height / 2);
            } else {
                valueAnimator = ValueAnimator.ofInt(height / 2, width - height / 2);
            }
            valueAnimator.setDuration(animDuration);
            valueAnimator.addUpdateListener(this);
            valueAnimator.addListener(this);
            valueAnimator.start();
        } else {
            dealToggle();
        }
    }

    private void dealToggle() {
        checked = !checked;
        resetPath();
        invalidate();

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this, checked);
        }
    }

    public int getAnimDuration() {
        return animDuration;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * px
     */
    public void setStrokeWidth(final int strokeWidth) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ToggleButton.this.strokeWidth = strokeWidth;
                circlePaint.setStrokeWidth(strokeWidth);
                resetPath();
                invalidate();
            }
        }, running ? animDuration - valueAnimator.getCurrentPlayTime() : 0);
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(final int circleColor) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ToggleButton.this.circleColor = circleColor;
                circlePaint.setColor(circleColor);
                invalidate();
            }
        }, running ? animDuration - valueAnimator.getCurrentPlayTime() : 0);
    }

    public int getBackUncheckedColor() {
        return backUncheckedColor;
    }

    public void setBackUncheckedColor(final int backUncheckedColor) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ToggleButton.this.backUncheckedColor = backUncheckedColor;
                invalidate();
            }
        }, running ? animDuration - valueAnimator.getCurrentPlayTime() : 0);
    }

    public int getBackCheckedColor() {
        return backCheckedColor;
    }

    public void setBackCheckedColor(final int backCheckedColor) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ToggleButton.this.backCheckedColor = backCheckedColor;
                invalidate();
            }
        }, running ? animDuration - valueAnimator.getCurrentPlayTime() : 0);
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        circlePath.reset();
        circlePath.addCircle((int) animation.getAnimatedValue(), height / 2, height / 2 - strokeWidth * 2, Path.Direction.CW);
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        running = false;
        dealToggle();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
