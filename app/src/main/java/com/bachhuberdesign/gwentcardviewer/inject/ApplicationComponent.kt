package com.bachhuberdesign.gwentcardviewer.inject

import com.bachhuberdesign.gwentcardviewer.App
import com.bachhuberdesign.gwentcardviewer.inject.module.NetworkModule
import com.bachhuberdesign.gwentcardviewer.inject.module.RepositoryModule
import com.google.gson.Gson
import dagger.Component
import javax.inject.Singleton

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@Singleton
@Component(modules = arrayOf(NetworkModule::class, RepositoryModule::class))
interface ApplicationComponent {

    fun inject(application: App)

    fun gson(): Gson

}