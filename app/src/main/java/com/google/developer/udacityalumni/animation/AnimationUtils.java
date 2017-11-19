package com.google.developer.udacityalumni.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.widget.RecyclerView;


/**
 * Created by Dell on 1/15/2017.
 */

public class AnimationUtils {

        public static void scaleXY(RecyclerView.ViewHolder holder) {
            holder.itemView.setScaleX(0);
            holder.itemView.setScaleY(0);

            PropertyValuesHolder propx = PropertyValuesHolder.ofFloat("scaleX", 1);
            PropertyValuesHolder propy = PropertyValuesHolder.ofFloat("scaleY", 1);

            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(holder.itemView, propx, propy);


            animator.setDuration(800);
            animator.start();
        }




}


