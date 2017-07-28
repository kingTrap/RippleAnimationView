package com.hxj.app;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/7/26.
 */

public class RippleCircleView extends View {

    RippleAnimationView mRippleAnimationView;

    public RippleCircleView(Context context) {
        super(context);
    }

    public RippleCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RippleCircleView(Context context , RippleAnimationView rippleAnimationView){
        this(context);
        this.mRippleAnimationView = rippleAnimationView;
        this.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onDraw(Canvas canvas) {

        int radius = Math.min(getWidth(),getHeight())/2;

        canvas.drawCircle(radius,radius,radius-mRippleAnimationView.getRippleStrokeWidth(),mRippleAnimationView.getPaint());
    }
}
