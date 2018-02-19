package com.google.developer.udacityalumni.logging;

import android.support.annotation.NonNull;
import android.util.Log;

import timber.log.Timber;

/**
 * Created by benjaminlewis on 2/5/18.
 */

public class ReleaseTree extends Timber.Tree {

    private static final int MAX_LOG_LENGTH = 4000;

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (isLoggable(tag, priority)) {
            if (message.length() < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Timber.tag(tag).wtf(message);
                } else {
                    Log.println(priority, tag, message);
                }
                return;
            }
        }

        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                if (priority == Log.ASSERT) {
                    Timber.tag(tag).wtf(part);
                } else {
                    Log.println(priority, tag, part);
                }
                i = end;
            } while (i < newline);
        }

    }

    @Override
    protected boolean isLoggable(String tag, int priority) {
        return !(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO);
    }
}
