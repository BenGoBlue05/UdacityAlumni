package com.google.developer.udacityalumni.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ApplicationModule {

    @Binds abstract Context context(Application application);

}
