package com.google.developer.udacityalumni.di;

import com.google.developer.udacityalumni.service.AlumIntentService;
import com.google.developer.udacityalumni.service.ArticleFirebaseJobService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilderModule {
    @ContributesAndroidInjector abstract AlumIntentService alumIntentService();
    @ContributesAndroidInjector abstract ArticleFirebaseJobService articleFirebaseJobService();
}
