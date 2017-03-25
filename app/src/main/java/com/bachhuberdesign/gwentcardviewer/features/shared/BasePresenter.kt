package com.bachhuberdesign.gwentcardviewer.features.shared

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class BasePresenter<in T : MvpContract> : Presenter<T> {

    var view: MvpContract? = null

    override fun attach(view: T) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun isViewAttached(): Boolean = view != null

}