package com.bachhuberdesign.gwentcardviewer.features.shared

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bachhuberdesign.gwentcardviewer.App
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.inject.ActivitySubcomponent
import com.bachhuberdesign.gwentcardviewer.inject.DaggerPersistedComponent
import com.bachhuberdesign.gwentcardviewer.inject.PersistedComponent
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
        @JvmStatic private val NEXT_ID: AtomicLong = AtomicLong(0)
        @JvmStatic private val components = HashMap<Long, PersistedComponent>()
    }

    var activityId: Long = 0
    lateinit var activitySubcomponent: ActivitySubcomponent
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

        activitySubcomponent = persistedComponent.activitySubcomponent(ActivityModule(this))
        activitySubcomponent.inject(this)


        setContentView(R.layout.activity_main)
    }

}
