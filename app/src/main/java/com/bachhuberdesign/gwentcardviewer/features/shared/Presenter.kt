package com.bachhuberdesign.gwentcardviewer.features.shared

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