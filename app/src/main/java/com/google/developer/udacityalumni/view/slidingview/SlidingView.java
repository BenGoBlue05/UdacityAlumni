package com.google.developer.udacityalumni.view.slidingview;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

/**
 * A sliding view is a card that animates on and off from the bottom of the screen. This class is
 * used to build adapters for use with a {@link SlidingViewManager}
 *
 * Created by Tom Calver on 17/01/17.
 */

public interface SlidingView {

    /**
     * Use to inflate a layout and bind any data
     * @param parent The enclosing layout that slides in from the bottom of the screen (the card)
     * @return The inflated View to place inside the sliding card, with bound data
     */
    View onCreateView(ViewGroup parent);

    /**
     *
     * @return Any data bound in {@link #onCreateView(ViewGroup)} that is to be retained across
     * configuration changes.
     */
    Parcelable getParcelableData();

    /**
     *
     * @param data Any data bound in {@link #onCreateView(ViewGroup)} that is to be retained across
     * configuration changes.
     */
    void setParcelableData(Parcelable data);

}
