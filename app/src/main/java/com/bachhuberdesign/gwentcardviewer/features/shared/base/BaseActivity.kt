package com.bachhuberdesign.gwentcardviewer.features.shared.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bachhuberdesign.gwentcardviewer.App
import com.bachhuberdesign.gwentcardviewer.inject.ActivityComponent
import com.bachhuberdesign.gwentcardviewer.inject.DaggerPersistedComponent
import com.bachhuberdesign.gwentcardviewer.inject.PersistedComponent
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        @JvmStatic val TAG: String = BaseActivity::class.java.name
        @JvmStatic private val NEXT_ID: AtomicLong = AtomicLong(0)
        @JvmStatic private val components = HashMap<Long, PersistedComponent>()
    }

    var activityId: Long = 0
    lateinit var activityComponent: ActivityComponent
    lateinit var persistedComponent: PersistedComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityId = savedInstanceState?.getLong(TAG) ?: NEXT_ID.getAndIncrement()

        if (!components.containsKey(activityId)) {
            persistedComponent = DaggerPersistedComponent.builder()
                    .applicationComponent(App.applicationComponent)
                    .build()
            components.put(activityId, persistedComponent)
        } else {
            persistedComponent = components[activityId] as PersistedComponent
        }

        activityComponent = persistedComponent.activitySubcomponent(ActivityModule(this))
        activityComponent.inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(TAG, activityId)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        when {
            !isChangingConfigurations -> components.remove(activityId)
        }
        super.onDestroy()
    }

}
