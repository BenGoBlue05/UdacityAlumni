package com.google.developer.udacityalumni.view.slidingview;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.google.developer.udacityalumni.view.ViewUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Manages a {@link SlidingView} by providing a container layout to load the View returned from
 * {@link SlidingView#onCreateView(ViewGroup)} into. Responsible for saving state changes and
 * animating the card.
 *
 * Created by Tom Calver on 18/01/17.
 */
public final class SlidingViewManager implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = SlidingViewManager.class.getSimpleName();
    private static final String STATE_KEY = TAG + ".STATE_KEY";
    private static final String OPTIONS_KEY = TAG + ".OPTIONS_KEY";
    private static final int ANIMATION_DURATION = 500;
    private static final int SLIDING_CARD_ELEVATION = ViewUtils.dpToPx(16);

    private boolean isExpanded = false, isAnimating = false;

    private SlidingView mAdapter;
    private final ViewGroup mSlidingView;
    private final View mScrim;
    private final Interpolator mFastOutSlowInInterpolator = new FastOutSlowInInterpolator();
    private final ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    private final int startColor = Color.TRANSPARENT;
    private final int endColor = Color.argb(120, 40, 40, 40);

    public SlidingViewManager(Activity activity) {
        this((ViewGroup) activity.findViewById(android.R.id.content));
    }

    public SlidingViewManager(android.app.Fragment fragment) {
        this((ViewGroup) fragment.getActivity().findViewById(android.R.id.content));
    }

    public SlidingViewManager(android.support.v4.app.Fragment fragment) {
        this((ViewGroup) fragment.getActivity().findViewById(android.R.id.content));
    }

    public SlidingViewManager(ViewGroup rootView) {

        mSlidingView = initSlidingView(rootView.getContext());
        mScrim = initScrim(rootView.getContext());

        rootView.addView(mScrim);
        rootView.addView(mSlidingView);

        mSlidingView.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    public void setAdapter(SlidingView adapter) {
        mAdapter = adapter;
        final View contentView = mSlidingView.getChildAt(0);
        if (contentView != null) {
            mSlidingView.removeView(contentView);
        }
        mSlidingView.addView(mAdapter.onCreateView(mSlidingView));
    }

    private void animateIn() {

        if (isExpanded || isAnimating) return;

        ViewCompat.setTranslationY(mSlidingView, mSlidingView.getHeight());
        ViewCompat.animate(mSlidingView)
                .translationY(0f)
                .setInterpolator(mFastOutSlowInInterpolator)
                .setDuration(ANIMATION_DURATION)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        mSlidingView.setVisibility(View.VISIBLE);
                        mScrim.setVisibility(View.VISIBLE);
                        isAnimating = true;
                    }
                    @Override
                    public void onAnimationEnd(View view) {
                        isExpanded = true;
                        isAnimating = false;
                    }
                })
                .setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(View view) {
                        final float transY = ViewCompat.getTranslationY(mSlidingView);
                        final float targetY = mSlidingView.getHeight();
                        final float delta = transY/targetY;
                        setScrimBackgroundColor(delta);

                    }
                }).start();
    }

    private void animateOut() {

        if(!isExpanded || isAnimating) return;

        ViewCompat.animate(mSlidingView)
                .translationY(mSlidingView.getHeight())
                .setInterpolator(mFastOutSlowInInterpolator)
                .setDuration(ANIMATION_DURATION)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        isAnimating = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        mSlidingView.setVisibility(View.GONE);
                        mScrim.setVisibility(View.GONE);
                        isExpanded = false;
                        isAnimating = false;
                    }
                })
                .setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(View view) {
                        final float transY = ViewCompat.getTranslationY(mSlidingView);
                        final float targetY = mSlidingView.getHeight();
                        final float delta = transY/targetY;
                        setScrimBackgroundColor(delta);
                    }
                }).start();

    }

    public void animate() {
        if(mAdapter == null) {
            Log.i(getClass().getSimpleName(), "Skipping animation - no adapter attached");
            return;
        }
        if (isExpanded) {
            animateOut();
        } else {
            animateIn();
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putBoolean(STATE_KEY, isExpanded);
        if (mAdapter != null) {
            bundle.putParcelable(OPTIONS_KEY, mAdapter.getParcelableData());
        }
    }

    public void onRestoreInstanceState(@NonNull Bundle bundle) {
        isExpanded = bundle.getBoolean(STATE_KEY);
        if(mAdapter != null) {
            mAdapter.setParcelableData(bundle.getParcelable(OPTIONS_KEY));
        }
        mSlidingView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void setScrimBackgroundColor(float delta) {
        final int color =
                (Integer) mArgbEvaluator.evaluate(delta, endColor, startColor);
        mScrim.setBackgroundColor(color);
    }

    private ViewGroup initSlidingView(Context context) {

        final FrameLayout container = new FrameLayout(context);
        final FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        container.setLayoutParams(params);
        ViewCompat.setElevation(container, SLIDING_CARD_ELEVATION);
        container.setBackgroundColor(Color.WHITE);

        return container;
    }

    private View initScrim(Context context) {

        final View scrim = new View(context);
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                MATCH_PARENT, MATCH_PARENT
        );
        scrim.setLayoutParams(params);
        scrim.setOnTouchListener(this);

        return scrim;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            animate();
        }
        return false;
    }

    @Override
    public void onGlobalLayout() {
        final int height = mSlidingView.getHeight();
        if(height > 0) {
            if(isExpanded) {
                ViewCompat.setTranslationY(mSlidingView, 0f);
                mSlidingView.setVisibility(View.VISIBLE);
                mScrim.setVisibility(View.VISIBLE);
                mScrim.setBackgroundColor(endColor);
            } else {
                ViewCompat.setTranslationY(mSlidingView, height);
                mSlidingView.setVisibility(View.GONE);
                mScrim.setVisibility(View.GONE);
                mScrim.setBackgroundColor(startColor);
            }
            mSlidingView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }
}
