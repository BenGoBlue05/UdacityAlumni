package com.google.developer.udacityalumni.view;

import android.content.res.Resources;

/**
 * Created by tom on 18/01/17.
 */

public final class ViewUtils {

    private ViewUtils() { throw new AssertionError(); }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
