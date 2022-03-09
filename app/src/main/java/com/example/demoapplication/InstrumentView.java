package com.example.demoapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class InstrumentView extends View {

    private Paint paint;

    private RectF rectF;

    private Path path;

    private int backgroundColor = Color.WHITE;

    private float progress = 5.0f;


    private final String low = "偏低";
    private final String normal = "正常";
    private final String lowHigh = "临高";
    private final String high = "轻高";
    private final String mediumHigh = "中高";
    private final String tooHigh = "重高";
    private ValueAnimator valueAnimator;
    private long animatorDuration;

    public InstrumentView(Context context) {
        super(context);


    }

    public InstrumentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        rectF = new RectF();
        path = new Path();

        paint.setAntiAlias(true);//设置抗锯齿
        paint.setDither(true);//设置防抖动

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InstrumentView);

        backgroundColor = typedArray.getColor(R.styleable.InstrumentView_backgroundColor, Color.WHITE);
        typedArray.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(backgroundColor);

        //用矩形约束圆弧绘制区域
        rectF.left = getWidth() / 20.0f;
        rectF.top = getWidth() / 40.0f + getWidth()/80.0f;
        rectF.bottom = getHeight() * 2;
        rectF.right = getWidth() - getWidth() / 20.0f;

        paint.setStrokeWidth(getWidth() / 15.0f);

        paint.setStyle(Paint.Style.STROKE);//设置画笔为空心
        paint.setColor(getResources().getColor(R.color.instrument_color_low));//设置画笔颜色
        //----------------------------------------绘制圆弧
        canvas.drawArc(rectF, 180, 30, false, paint);

        paint.setColor(getResources().getColor(R.color.instrument_color_normal));
        canvas.drawArc(rectF, 210, 30, false, paint);

        paint.setColor(getResources().getColor(R.color.instrument_color_low_high));
        canvas.drawArc(rectF, 240, 30, false, paint);

        paint.setColor(getResources().getColor(R.color.instrument_color_high));
        canvas.drawArc(rectF, 270, 30, false, paint);


        paint.setColor(getResources().getColor(R.color.instrument_color_medium_high));
        canvas.drawArc(rectF, 300, 30, false, paint);

        paint.setColor(getResources().getColor(R.color.instrument_color_too_high));
        canvas.drawArc(rectF, 330, 30, false, paint);
        //----------------------------------------绘制圆弧


        canvas.save();
        paint.setColor(Color.GRAY);
        canvas.translate(getWidth() / 2.0f, getHeight());//移动圆点
        paint.setStrokeWidth(getWidth() / 300.0f);

        //绘制刻度
        for (int i = 1; i <= 5; i++) {
            canvas.rotate(30);
            canvas.drawLine(-(getWidth() / 2.0f - getWidth() / 9.0f), 0, -(getWidth() / 2.0f - getWidth() / 6.5f), 0, paint);
        }
        canvas.restore();


        //绘制表针
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        canvas.translate(getWidth() / 2.0f, getHeight());
        canvas.rotate(progress);
        paint.setColor(getResources().getColor(R.color.instrument_color_needle));
        path.moveTo(0, getWidth() / 50.0f);
        path.lineTo(0, -(getWidth() / 50.0f));
        path.lineTo(-(getWidth() / 2.0f) + (getWidth() / 25.0f), 0);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restore();


        //绘制中心圆点
        paint.setColor(getResources().getColor(R.color.instrument_color_center));
        canvas.drawCircle(getWidth() / 2.0f, getHeight(), getWidth() / 25.0f, paint);


        //绘制文字
        paint.setColor(getResources().getColor(R.color.instrument_color_font));
        paint.setStrokeWidth(5);
        paint.setTextSize(getWidth() / 30.0f);
        canvas.translate(getWidth() / 2.0f, getHeight());
        canvas.translate(-getWidth() / 2.0f, 0);

        canvas.drawText(low, getWidth() / 12.0f + getWidth() / 15.0f, -(getHeight() / 6.0f), paint);

        canvas.drawText(normal, getWidth() * 3 / 12.0f - getWidth() / 30.0f, -(getHeight() * 3 / 6.0f), paint);

        canvas.drawText(lowHigh, getWidth() * 5 / 12.0f - getWidth() / 20.0f, -(getHeight() * 5 / 6.0f) + getWidth() / 15.0f, paint);

        canvas.drawText(high, getWidth() * 7 / 12.0f - getWidth() / 20.0f, -(getHeight() * 5 / 6.0f) + getWidth() / 15.0f, paint);

        canvas.drawText(mediumHigh, getWidth() * 9 / 12.0f - getWidth() / 30.0f, -(getHeight() * 3 / 6.0f), paint);

        canvas.drawText(tooHigh, getWidth() * 11 / 12.0f - getWidth() / 8.0f, -(getHeight() / 6.0f), paint);


    }


    public void setCurrentProgress(float progress) {

        setAnimator(progress);

    }



    public void setBackColor(@ColorInt int color) {

        this.backgroundColor = color;
        invalidate();

    }

    private void setAnimator(final float value) {

        //根据变化的幅度来调整动画时长
        animatorDuration = (long) Math.abs(value - progress) * 40;

        valueAnimator = ValueAnimator.ofFloat(progress, value).setDuration(animatorDuration);
        valueAnimator.setInterpolator(new SpringInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                InstrumentView.this.progress = (float) animation.getAnimatedValue();
                invalidate();
            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progress = value;
            }
        });
        valueAnimator.start();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getWidthSize(widthMeasureSpec);
     /*  int height = getHeightSize(heightMeasureSpec);

        if (width < height) {
            height = width/2;
        }*/

        int height = width / 2;

        setMeasuredDimension(width, height);

    }

    private int getWidthSize(int measureSpec) {

        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(400, specSize);
        } else {
            result = 400;
        }
        return result;
    }

   /* private int getHeightSize(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;//确切大小,所以将得到的尺寸给view
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(200, specSize);
        } else {
            result = 200;
        }
        return result;
    }*/

    public class SpringInterpolator implements Interpolator {
        private final float mTension;

        public SpringInterpolator() {
            mTension = 0.5f;
        }

        public SpringInterpolator(float tension) {
            mTension = tension;
        }

        @Override
        public float getInterpolation(float input) {
            float result = (float) (Math.pow(2, -13 * input) *
                    Math.sin((input - mTension / 4) * (2 * Math.PI) / mTension) + 1);
            return result;

        }
    }

}
