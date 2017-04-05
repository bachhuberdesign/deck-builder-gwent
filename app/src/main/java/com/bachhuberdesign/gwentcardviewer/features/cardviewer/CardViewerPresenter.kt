package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckRepository
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class CardViewerPresenter
@Inject constructor(private val repository: DeckRepository) : BasePresenter<CardViewerMvpContract>() {

    override fun attach(view: CardViewerMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    fun getUsableCards(factionId: Int) {
        // TODO:
    }

}

