package com.google.developer.udacityalumni.view;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Tom Calver on 18/01/17.
 */

public final class UAViewUtils {

    private UAViewUtils() {
        throw new AssertionError();
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void setVisibleGone(@Nullable View view, boolean isVisible) {
        if (view != null) {
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        } else if (isVisible) {
            throw new IllegalArgumentException("View cannot be visible if it is null");
        }
    }

}
