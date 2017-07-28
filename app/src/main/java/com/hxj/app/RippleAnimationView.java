package com.hxj.app;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class RippleAnimationView extends RelativeLayout {

    int rippleType;
    int rippleColor;
    float rippleScale;
    float rippleRadius;
    int rippleDuration;
    int rippleCount;
    float rippleStrokeWidth;

    Paint paint;
    Paint.Style rippleStyle;
    boolean isRippleAnimation ;
    List<RippleCircleView> mRippleCicleViews = new ArrayList<RippleCircleView>();
    AnimatorSet mAnimatorSet;

    public float getRippleRadius() {
        return rippleRadius;
    }

    public float getRippleStrokeWidth() {
        return rippleStrokeWidth;
    }

    public Paint getPaint() {
        return paint;
    }

    private static final int DEFAULT_TYPE = Paint.Style.FILL.ordinal();
    private static final int DEFAULT_COLOR = Color.RED;
    private static final int DEFAULT_COUNT = 5;
    private static final float DEFAULT_SCALE = 5f;
    private static final int DEFAULT_DURATION = 2500;
    private static final float DEFAULT_RADIUS = 100;
    private static final float DEFAULT_STROKE_WIDTH = 10;

    public RippleAnimationView(Context context) {
        super(context);
    }

    public RippleAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if(isInEditMode()){
            return ;
        }
        //读取自定义属性

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.RippleAnimationView);
        rippleType = a.getInt(R.styleable.RippleAnimationView_ripple_anim_type,DEFAULT_TYPE);
        rippleColor = a.getColor(R.styleable.RippleAnimationView_ripple_anim_color,DEFAULT_COLOR);
        rippleScale = a.getFloat(R.styleable.RippleAnimationView_ripple_anim_scale,DEFAULT_SCALE);
        rippleCount = a.getInt(R.styleable.RippleAnimationView_ripple_anim_count,DEFAULT_COUNT);
        rippleDuration = a.getInt(R.styleable.RippleAnimationView_ripple_anim_duration,DEFAULT_DURATION);
        rippleRadius = a.getDimension(R.styleable.RippleAnimationView_ripple_anim_radius,DEFAULT_RADIUS);
        rippleStrokeWidth = a.getDimension(R.styleable.RippleAnimationView_ripple_anim_stroke_width,DEFAULT_STROKE_WIDTH);
        if(rippleType == Paint.Style.FILL.ordinal()){
            rippleStrokeWidth = 0;
            rippleStyle = Paint.Style.FILL;
        }else{
            rippleStyle = Paint.Style.STROKE;
        }
        a.recycle();

        //构造出画笔
        paint = new Paint();
        paint.setStyle(rippleStyle);
        paint.setColor(rippleColor);
        paint.setAntiAlias(true);// 防止锯齿
        paint.setStrokeWidth(rippleStrokeWidth);

        //每张图片动画需要延迟播放的时间，这样才会有水波纹效果
        long startDelayGasp = rippleDuration/rippleCount;

        //根据实际半径，计算水波纹大小
        LayoutParams params = new LayoutParams((int)(2*(rippleRadius+rippleStrokeWidth)),(int)(2*(rippleRadius+rippleStrokeWidth)));
        params.addRule(CENTER_IN_PARENT,TRUE);

        List<Animator>  mViewAnimators = new ArrayList<Animator>();
        //实例化圆形View
        for(int i = 0 ;i<rippleCount;i++){
            RippleCircleView circleView = new RippleCircleView(context,this);
            addView(circleView,params);
            mRippleCicleViews.add(circleView);

            //创建X轴缩放动画，并绑定View
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(circleView,"ScaleX",1.0f,rippleScale);
            scaleX.setDuration(rippleDuration);
            scaleX.setRepeatCount(ValueAnimator.INFINITE);
            scaleX.setRepeatMode(ValueAnimator.RESTART);
            scaleX.setStartDelay( i*startDelayGasp);
            mViewAnimators.add(scaleX);

            //创建Y轴缩放动画，并绑定View
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(circleView,"ScaleY",1.0f,rippleScale);
            scaleY.setDuration(rippleDuration);
            scaleY.setRepeatCount(ValueAnimator.INFINITE);
            scaleY.setRepeatMode(ValueAnimator.RESTART);
            scaleY.setStartDelay( i*startDelayGasp);
            mViewAnimators.add(scaleY);

            //Alpha渐变
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(circleView, "Alpha", 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);//无限重复
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(i * startDelayGasp);
            alphaAnimator.setDuration(rippleDuration);
            mViewAnimators.add(alphaAnimator);
        }

        mAnimatorSet = new AnimatorSet();
        //开始播放动画
        mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorSet.playTogether(mViewAnimators);

    }

    public void stopRippleAnimation(){
        if(isRippleAnimation()){
            Collections.reverse(mRippleCicleViews);
            for (RippleCircleView rippleView : mRippleCicleViews) {
                rippleView.setVisibility(INVISIBLE);
            }
            mAnimatorSet.end();
            isRippleAnimation = false;
        }
    }

    public void startRippleAnimation(){
        if(!isRippleAnimation()){
            for (RippleCircleView rippleView : mRippleCicleViews) {
                rippleView.setVisibility(VISIBLE);
            }
            mAnimatorSet.start();
            isRippleAnimation = true;
        }
    }

    public boolean isRippleAnimation(){
        return isRippleAnimation;
    }
}
