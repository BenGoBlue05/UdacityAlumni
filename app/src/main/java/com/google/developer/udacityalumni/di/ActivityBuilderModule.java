package com.google.developer.udacityalumni.di;

import com.google.developer.udacityalumni.activity.NewAppActivity;
import com.google.developer.udacityalumni.activity.WebViewActivity;
import com.google.developer.udacityalumni.fakedata.FakeDataActivity;
import com.google.developer.udacityalumni.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {
    @ContributesAndroidInjector abstract FakeDataActivity fakeDataActivity();
    @ContributesAndroidInjector abstract MainActivity mainActivity();
    @ContributesAndroidInjector abstract NewAppActivity newAppActivity();
    @ContributesAndroidInjector abstract WebViewActivity webViewActivity();
}
