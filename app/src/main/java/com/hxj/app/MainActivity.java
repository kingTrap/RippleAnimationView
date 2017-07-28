package com.hxj.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    RippleAnimationView mRippleAnimView;
    ImageView  mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRippleAnimView = (RippleAnimationView) findViewById(R.id.ripple_anim_view);
        mImageView = (ImageView) findViewById(R.id.iv);
    }

    public void onClick(View v){
        if(v == mImageView){
            if(mRippleAnimView.isRippleAnimation()){
                mRippleAnimView.stopRippleAnimation();
            }else{
                mRippleAnimView.startRippleAnimation();
            }
        }
    }
}
