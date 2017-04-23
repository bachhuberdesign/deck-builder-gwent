package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckRepository
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckCardListPresenter
@Inject constructor(private val deckRepository: DeckRepository) : BasePresenter<DeckCardListMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckCardListPresenter::class.java.name
    }

    override fun attach(view: DeckCardListMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    fun loadDeck(deckId: Int) {
        val deck = deckRepository.getDeckById(deckId)

        if (deck == null) {
            if (isViewAttached()) {
                view!!.showErrorMessage("Unable to load deck $deckId.")
            }
        } else if (isViewAttached()) {
            view!!.onDeckLoaded(deck)
        }
    }

    fun removeCardFromDeck(card: Card, deckId: Int) {
        deckRepository.deleteCardFromDeck(card, deckId)
    }

    private fun sortCards() {

    }

}