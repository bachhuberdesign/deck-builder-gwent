package com.bachhuberdesign.gwentcardviewer.inject.module

import android.app.Activity
import android.content.Context
import com.bachhuberdesign.gwentcardviewer.inject.annotation.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@Module
class ActivityModule(val activity: Activity) {

    @Provides
    internal fun provideActivity(): Activity {
        return activity
    }

    @Provides
    @ActivityScope
    internal fun providesContext(): Context {
        return activity
    }

}
