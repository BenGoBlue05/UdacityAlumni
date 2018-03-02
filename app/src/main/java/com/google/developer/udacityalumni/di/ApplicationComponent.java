package com.google.developer.udacityalumni.di;

import android.app.Application;

import com.google.developer.udacityalumni.UdacityAlumni;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidBuilderModule.class,
        AndroidInjectionModule.class,
        ApplicationModule.class
})
public interface ApplicationComponent {

    void inject(UdacityAlumni app);

    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        ApplicationComponent build();
    }

}
