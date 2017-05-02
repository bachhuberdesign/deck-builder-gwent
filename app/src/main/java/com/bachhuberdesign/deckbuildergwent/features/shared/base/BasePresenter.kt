package com.bachhuberdesign.deckbuildergwent.features.shared.base

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
open class BasePresenter<T : MvpContract> : Presenter<T> {

    var view: T? = null

    override fun attach(view: T) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun isViewAttached(): Boolean = view != null

    override fun getViewOrThrow(): T {
        if (view == null) {
            throw UnsupportedOperationException("Tried to get view for presenter but was null.")
        } else {
            return view!!
        }
    }

}