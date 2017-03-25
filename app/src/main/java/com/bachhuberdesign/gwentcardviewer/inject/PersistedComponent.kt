package com.bachhuberdesign.gwentcardviewer.inject

import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistingScope
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import dagger.Component

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistingScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface PersistedComponent {

    fun activitySubcomponent(activityModule: ActivityModule): ActivitySubcomponent

}