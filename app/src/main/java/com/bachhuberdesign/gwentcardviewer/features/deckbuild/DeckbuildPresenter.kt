package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistingScope

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistingScope
class DeckbuildPresenter : BasePresenter<DeckbuildMvpContract>() {

    override fun attach(view: DeckbuildMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    fun loadUserDecks() {
        // TODO:

        if (isViewAttached()) {
            view!!.onDecksLoaded()
        }
    }

}