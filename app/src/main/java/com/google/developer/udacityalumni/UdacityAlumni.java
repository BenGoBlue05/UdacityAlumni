package com.google.developer.udacityalumni;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.google.developer.udacityalumni.di.DaggerApplicationComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class UdacityAlumni extends Application implements HasActivityInjector, HasServiceInjector {

    @Inject DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    @Inject DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder().application(this).build().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }

}
