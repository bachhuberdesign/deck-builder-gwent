package com.bachhuberdesign.deckbuildergwent.features.deckdetail

import android.util.Log
import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardRepository
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.exception.CardException
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckDetailPresenter
@Inject constructor(private val deckRepository: DeckRepository,
                    private val cardRepository: CardRepository) : BasePresenter<DeckDetailMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckDetailPresenter::class.java.name
    }

    /**
     *
     */
    fun loadDeck(deckId: Int) {
        val deck = deckRepository.getDeckById(deckId)

        if (deck != null) {
            getViewOrThrow().onDeckLoaded(deck)
        } else {
            view!!.showErrorMessage("Unable to load deck $deckId.")
        }
    }

    /**
     *
     */
    fun removeCardFromDeck(card: Card?, deckId: Int) {
        Log.d(TAG, "Removing card ${card?.cardId} from deck with selectedLane: ${card?.selectedLane}")
        if (card != null) {
            deckRepository.removeCardFromDeck(card, deckId)
        }
    }

    /**
     *
     */
    fun getLeadersForFaction(factionId: Int) {
        val leaders: List<Card> = deckRepository.getLeadersForFaction(factionId)

        getViewOrThrow().onLeadersLoaded(leaders)
    }

    /**
     *
     */
    fun updateLeaderForDeck(deckId: Int, leaderId: Int) {
        if (cardRepository.getCardById(leaderId).cardType != CardType.LEADER) {
            throw CardException("Tried to add card ID $leaderId but is not a leader. ")
        }

        deckRepository.updateLeaderForDeck(deckId, leaderId)
        val deck = deckRepository.getDeckById(deckId)

        getViewOrThrow().onDeckLoaded(deck!!)
        getViewOrThrow().showDeckNameChangeDialog()
    }

}