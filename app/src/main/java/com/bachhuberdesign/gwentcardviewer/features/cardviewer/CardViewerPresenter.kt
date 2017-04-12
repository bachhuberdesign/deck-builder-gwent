package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
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
class CardViewerPresenter
@Inject constructor(val cardRepository: CardRepository,
                    val deckRepository: DeckRepository) : BasePresenter<CardViewerMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = CardViewerPresenter::class.java.name
    }

    /**
     *
     */
    fun checkCardAddable(card: Card, deckId: Int) {
        val deck = deckRepository.getDeckById(deckId)

        if (deck != null && Deck.isCardAddableToDeck(deck, card)) {
            if (isViewAttached()) {
                view!!.onCardChecked(card)
            }
        }
    }

    fun getAllCards() {
        TODO("Method not yet implemented")
    }

    fun getCardsFiltered(filters: CardFilters) {
        // TODO: Refactor to query full card list and then filter rather

        if (filters.filterByDeck.first) {
            // TODO: Get cards for deck only
        } else if (filters.filterByFactions.first) {
            val cursor = cardRepository.getAllFactionAndNeutralCards(filters.filterByFactions.second)
            val cards: MutableList<Card> = ArrayList()

            cursor.use {
                while (cursor.moveToNext()) {
                    cards.add(Card.MAPPER.apply(cursor))
                }
            }

            if (isViewAttached()) {
                view!!.onCardsLoaded(cards)
            }
        }

    }

}

