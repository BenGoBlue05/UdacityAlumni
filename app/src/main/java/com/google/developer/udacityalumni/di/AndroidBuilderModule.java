package com.google.developer.udacityalumni.di;

import dagger.Module;

@Module(includes = {
        ActivityBuilderModule.class,
        ServiceBuilderModule.class
})
public abstract class AndroidBuilderModule {}
