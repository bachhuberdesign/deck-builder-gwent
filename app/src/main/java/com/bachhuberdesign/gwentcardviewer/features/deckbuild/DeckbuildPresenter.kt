package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.cardviewer.CardRepository
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Lane
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckbuildPresenter
@Inject constructor(private val deckRepository: DeckRepository,
                    private val cardRepository: CardRepository) : BasePresenter<DeckbuildMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    /**
     *
     */
    fun loadUserDeck(deckId: Int) {
        val deck: Deck? = deckRepository.getDeckById(deckId)

        if (deck == null && isViewAttached()) {
            view!!.onErrorLoadingDeck("Error loading deck $deckId")
        } else if (deck != null && isViewAttached()) {
            view!!.onDeckLoaded(deck)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.MELEE), Lane.MELEE)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.RANGED), Lane.RANGED)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.SIEGE), Lane.SIEGE)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.EVENT), Lane.EVENT)
        }
    }

    private fun filterCardsByLane(deck: Deck, lane: Int): List<Card> {
        val filteredList: MutableList<Card> = ArrayList()
        deck.cards.forEach { card ->
            if (card.selectedLane == lane) {
                filteredList.add(card)
            }
        }
        return filteredList
    }

    /**
     *
     */
    fun addCard(deckId: Int, cardId: Int) {
        val deck: Deck = deckRepository.getDeckById(deckId) ?: throw Exception("Deck $deckId not found.")

        val card = Card(cardId = cardId)
        deck.cards.add(card)

        deckRepository.saveDeck(deck)
        if (isViewAttached()) {
            view!!.onCardAdded(cardRepository.getCardById(cardId))
        }
    }

    /**
     *
     */
    fun removeCard(cardId: Int) {
        Log.d(TAG, "removeCard() called for card $cardId")
        // TODO:

        if (isViewAttached()) {
            view!!.onCardRemoved()
        }
    }

    /**
     *
     */
    fun saveDeck(deck: Deck) {
        deckRepository.saveDeck(deck)
    }

    /**
     *
     */
    fun deleteDeck(deckId: Int) {
        deckRepository.deleteDeck(deckId)

        if (isViewAttached()) {
            view!!.onDeckDeleted(deckId)
        }
    }

}