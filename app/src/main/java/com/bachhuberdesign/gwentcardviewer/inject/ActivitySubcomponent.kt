package com.bachhuberdesign.gwentcardviewer.inject

import com.bachhuberdesign.gwentcardviewer.features.shared.BaseActivity
import com.bachhuberdesign.gwentcardviewer.inject.annotation.ActivityScope
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import dagger.Subcomponent

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivitySubcomponent {

    fun inject(baseActivity: BaseActivity)

}
