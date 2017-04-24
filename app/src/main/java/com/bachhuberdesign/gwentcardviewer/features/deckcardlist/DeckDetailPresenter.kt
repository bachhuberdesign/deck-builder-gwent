package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.util.Log
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
class DeckDetailPresenter
@Inject constructor(private val deckRepository: DeckRepository) : BasePresenter<DeckDetailMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckDetailPresenter::class.java.name
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

    fun removeCardFromDeck(card: Card?, deckId: Int) {
        Log.d(TAG, "Removing card ${card?.cardId} from deck with selectedLane: ${card?.selectedLane}")
        if (card != null) {
            deckRepository.deleteCardFromDeck(card, deckId)
        }
    }

}