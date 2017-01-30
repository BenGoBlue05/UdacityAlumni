package com.google.developer.udacityalumni.view.slidingview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

/**
 * Animates a {@link BottomNavigationView}
 *
 * Created by Tom Calver on 30/01/17.
 */

public final class BottomNavBarManager implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final int ANIMATION_DURATION = 250;

    private final Interpolator mFastOutSlowInInterpolator = new FastOutSlowInInterpolator();
    private final BottomNavigationView mView;
    private final OnBottomNavBarLaidOutListener mListener;

    private boolean isExpanded;

    @SuppressWarnings("unused")
    public BottomNavBarManager(@NonNull BottomNavigationView view) {
        this(view,null);
    }

    /**
     *
     * @param view The view to manage
     * @param listener Notifies caller when view is laid out
     */
    public BottomNavBarManager(@NonNull BottomNavigationView view, @Nullable OnBottomNavBarLaidOutListener listener) {
        checkParentIsCoordinatorLayoutOrThrow(view);
        mView = view;
        mListener = listener;
        if(mListener != null) {
            mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

    }

    /**
     * Hides the {@link BottomNavigationView}. with an animation
     */
    public void hide() {

        if(!isExpanded) return;

        ViewCompat.animate(mView)
                .translationY(mView.getHeight())
                .setInterpolator(mFastOutSlowInInterpolator)
                .setDuration(ANIMATION_DURATION)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(View view) {
                        mView.setVisibility(View.GONE);
                        isExpanded = false;
                    }
                }).start();

    }

    /**
     * Shows the {@link BottomNavigationView}, with an animation
     */
    public void show() {

        if (isExpanded) return;

        ViewCompat.setTranslationY(mView, mView.getHeight());
        ViewCompat.animate(mView)
                .translationY(0f)
                .setInterpolator(mFastOutSlowInInterpolator)
                .setDuration(ANIMATION_DURATION)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        mView.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationEnd(View view) {
                        isExpanded = true;
                    }
                }).start();

    }

    /**
     * Shows the {@link BottomNavigationView} immediately, without animation
     */
    public void setShown() {
        ViewCompat.setTranslationY(mView, 0f);
        mView.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the {@link BottomNavigationView} immediately, without animation
     */
    public void setHidden() {
        ViewCompat.setTranslationY(mView, mView.getHeight());
        mView.setVisibility(View.GONE);
    }

    private static void checkParentIsCoordinatorLayoutOrThrow(View view) {
        final boolean isParentCoordinatorLayout =
                (view.getLayoutParams() instanceof CoordinatorLayout.LayoutParams);
        if(!isParentCoordinatorLayout) {
            throw new IllegalArgumentException(view.toString() +
                    " must have a LayoutParams of type " +
                    CoordinatorLayout.LayoutParams.class.getSimpleName());
        }
    }

    @Override
    public void onGlobalLayout() {
        if(mView.isLaidOut()){
            mListener.onBottomNavBarLaidOut();
            mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /**
     * Callback for when {@link BottomNavigationView} has completed its layout
     */
    public interface OnBottomNavBarLaidOutListener {
        void onBottomNavBarLaidOut();
    }

}
