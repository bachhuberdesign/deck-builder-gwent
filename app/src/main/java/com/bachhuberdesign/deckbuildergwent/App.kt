package com.bachhuberdesign.deckbuildergwent

import android.app.Application
import com.bachhuberdesign.deckbuildergwent.inject.ApplicationComponent
import com.bachhuberdesign.deckbuildergwent.inject.DaggerApplicationComponent
import com.bachhuberdesign.deckbuildergwent.inject.module.DatabaseModule
import com.bachhuberdesign.deckbuildergwent.inject.module.NetworkModule
import com.bachhuberdesign.deckbuildergwent.inject.module.RepositoryModule
import com.bumptech.glide.request.target.ViewTarget
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class App : Application() {

    companion object {
        @JvmStatic val TAG: String = App::class.java.name
        @JvmStatic lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        // Enable Stetho debugging
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        // Workaround for Glide setTag issue: See http://stackoverflow.com/questions/34833627
        ViewTarget.setTagId(R.id.glide_tag)

        applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(NetworkModule())
                .repositoryModule(RepositoryModule())
                .databaseModule(DatabaseModule(this))
                .build()
        applicationComponent.inject(this)
    }

}
