package com.example.lwq.damaiclient.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by lwq on 2015/12/8.
 */
public class AnimationUtils {
    public static void btnAlpha(View view) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1000);
        view.startAnimation(aa);
    }

    public static void btnRotate(View view) {
        RotateAnimation ra = new RotateAnimation(0, 360, 100, 100);
        ra.setDuration(1000);
        view.startAnimation(ra);
    }

    public static void btnRotateSelf(View view) {
        RotateAnimation ra = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5F,
                RotateAnimation.RELATIVE_TO_SELF, 0.5F);
        ra.setDuration(1000);
        view.startAnimation(ra);
    }

    public static void btnTranslate(View view) {
        TranslateAnimation ta = new TranslateAnimation(0, 200, 0, 300);
        ta.setDuration(1000);
        view.startAnimation(ta);
    }

    public static void btnScale(View view) {
        ScaleAnimation sa = new ScaleAnimation(0, 2, 0, 2);
        sa.setDuration(1000);
        view.startAnimation(sa);
    }

    public static void btnScaleSelf(View view) {
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F);
        sa.setDuration(1000);
        view.startAnimation(sa);
    }

    public static void btnSet(View view) {
        AnimationSet as = new AnimationSet(true);
        as.setDuration(1000);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1000);
        as.addAnimation(aa);

        TranslateAnimation ta = new TranslateAnimation(0, 100, 0, -200);
        ta.setDuration(1000);
        as.addAnimation(ta);

        view.startAnimation(as);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void animMethod(final View v) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                v,
                "translationX",
                300
        );
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(v, "translationY", 300f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(v, "rotation", 0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5F,
                RotateAnimation.RELATIVE_TO_SELF, 0.5F);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(10000);
        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                if (v instanceof Button) {
                    ((Button) v).setText("Start");
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (v instanceof Button) {
                    ((Button) v).setText("End");
                }
            }
        });
    }

    public static void startCusstomAnim(View v){
        CusstomAnim anim = new CusstomAnim();
        anim.setRotateY(30);
        v.startAnimation(anim);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showDropView(View view ,Context context) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = dropAnim(view, 0, DisplayUtil.dip2px(context, 40));
        valueAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hideDropView(final View view,Context context) {
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }
        ValueAnimator valueAnimator = dropAnim(view, DisplayUtil.dip2px(context, 40), 0);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });

        valueAnimator.start();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void closeMenuAnim( List<ImageView> mImageViews) {
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(
                mImageViews.get(0),
                "alpha",
                0.5f,
                1f
        );
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                mImageViews.get(1),
                "translationY",
                0
        );
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                mImageViews.get(2),
                "translationX",
                0
        );
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(
                mImageViews.get(3),
                "translationY",
                0
        );
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(
                mImageViews.get(4),
                "translationX",
                0
        );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.playTogether(
                animator0,
                animator1,
                animator2,
                animator3,
                animator4
        );
        animatorSet.start();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startMenuAnim( List<ImageView> mImageViews) {
        ObjectAnimator animator0 = ObjectAnimator.ofFloat(
                mImageViews.get(0),
                "alpha",
                1f,
                0.5f
        );
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                mImageViews.get(1),
                "translationY",
                200f
        );
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                mImageViews.get(2),
                "translationX",
                200f
        );
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(
                mImageViews.get(3),
                "translationY",
                -200f
        );
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(
                mImageViews.get(4),
                "translationX",
                -200f
        );
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.playTogether(
                animator0,
                animator1,
                animator2,
                animator3,
                animator4
        );
        animatorSet.start();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ValueAnimator dropAnim(final View view, int start, int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return valueAnimator;
    }

}
