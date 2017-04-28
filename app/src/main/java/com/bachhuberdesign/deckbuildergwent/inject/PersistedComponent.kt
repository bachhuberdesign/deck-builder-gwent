package com.bachhuberdesign.deckbuildergwent.inject

import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import dagger.Component

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface PersistedComponent {

    fun activitySubcomponent(activityModule: ActivityModule): ActivityComponent

}