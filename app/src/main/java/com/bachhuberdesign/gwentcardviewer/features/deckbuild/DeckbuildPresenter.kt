package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckbuildPresenter
@Inject constructor(private val repository: DeckRepository) : BasePresenter<DeckbuildMvpContract>() {

    override fun attach(view: DeckbuildMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    fun loadUserDecks() {
        val decks = repository.getUserCreatedDecks()

        if (decks.isNotEmpty() && isViewAttached()) {
            view!!.onDecksLoaded(decks)
        }
    }

}