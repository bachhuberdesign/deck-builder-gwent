package com.bachhuberdesign.deckbuildergwent.features.shared.base

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface Presenter<T : MvpContract> {

    fun attach(view: T)

    fun detach()

    fun isViewAttached(): Boolean

    fun getViewOrThrow(): T

}