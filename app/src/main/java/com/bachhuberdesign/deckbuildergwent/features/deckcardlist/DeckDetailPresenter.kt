package com.bachhuberdesign.deckbuildergwent.features.deckcardlist

import android.util.Log
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
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