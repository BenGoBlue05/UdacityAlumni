package com.google.developer.udacityalumni.notification;

import android.content.Context;

/**
 *
 * Builds a new instance of {@link SpotlightNotificationManager}
 *
 * Created by Tom Calver on 11/01/17.
 */

public final class SpotlightNotificationFactory {

    private SpotlightNotificationFactory() { throw new AssertionError(); }

    public static SpotlightNotificationManager newInstance(Context context) {
        return new SpotlightNotificationManagerImpl(context);
    }

}
