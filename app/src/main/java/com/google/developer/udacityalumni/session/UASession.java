package com.google.developer.udacityalumni.session;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;

import com.google.developer.udacityalumni.BuildConfig;
import com.google.developer.udacityalumni.logging.ReleaseTree;

import timber.log.Timber;

/**
 * Created by benjaminlewis on 2/5/18.
 */

public class UASession {

    @SuppressLint("StaticFieldLeak")
    private static UASession instance;

    @SuppressLint("StaticFieldLeak")
    private static Application application;

    private UASession() {
    }

    public static void init(Application app) {
        if (instance == null) {
            instance = new UASession();
            application = app;
            setUpLogging();
        }
    }

    private static void setUpLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(@NonNull StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        } else {
            Timber.plant(new ReleaseTree());
        }
    }

    public static UASession getInstance() {
        return instance;
    }

    public static Application getApplication() {
        return application;
    }
}
