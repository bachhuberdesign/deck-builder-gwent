package com.bachhuberdesign.gwentcardviewer

import android.app.Application
import com.bachhuberdesign.gwentcardviewer.inject.ApplicationComponent
import com.bachhuberdesign.gwentcardviewer.inject.DaggerApplicationComponent
import com.bachhuberdesign.gwentcardviewer.inject.module.NetworkModule
import com.bachhuberdesign.gwentcardviewer.inject.module.RepositoryModule

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class App : Application() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
        @JvmStatic lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(NetworkModule())
                .repositoryModule(RepositoryModule())
                .build()
        applicationComponent.inject(this)
    }

}
