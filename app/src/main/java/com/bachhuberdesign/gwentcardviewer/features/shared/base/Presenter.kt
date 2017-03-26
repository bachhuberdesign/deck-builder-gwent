package com.bachhuberdesign.gwentcardviewer.features.shared.base

import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface Presenter<in T: MvpContract> {

    fun attach(view: T)

    fun detach()

    fun isViewAttached(): Boolean

}