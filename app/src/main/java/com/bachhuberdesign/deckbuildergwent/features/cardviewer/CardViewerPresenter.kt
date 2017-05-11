package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Lane
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import java.util.*
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
        val cardAddable = Deck.isCardAddableToDeck(deck!!, card)

        if (cardAddable && displayMultiLaneSelection(card).isNotEmpty()) {
            if (isViewAttached()) {
                view!!.showLaneSelection(displayMultiLaneSelection(card), card)
            }
        } else if (isViewAttached()) {
            view!!.onCardChecked(card, cardAddable)
        }

    }

    /**
     * @param deckId
     * @param card
     */
    fun addCardToDeck(deckId: Int, card: Card) {
        deckRepository.addCardToDeck(card, deckId)

        getViewOrThrow().updateCount(card, itemRemoved = false)
    }

    /**
     * @param deckId
     * @param card
     */
    fun removeCardFromDeck(deckId: Int, card: Card) {
        deckRepository.removeCardFromDeck(card, deckId)

        getViewOrThrow().updateCount(card, itemRemoved = true)
    }

    /**
     *
     */
    fun displayMultiLaneSelection(card: Card): List<Int> {
        val lanesToDisplay: MutableList<Int> = LinkedList()

        when (card.lane) {
            Lane.ALL -> lanesToDisplay.addAll(listOf(Lane.MELEE, Lane.RANGED, Lane.SIEGE, Lane.EVENT))
            Lane.MELEE_RANGED -> lanesToDisplay.addAll(listOf(Lane.MELEE, Lane.RANGED))
            Lane.MELEE_RANGED_SIEGE -> lanesToDisplay.addAll(listOf(Lane.MELEE, Lane.RANGED, Lane.SIEGE))
            Lane.RANGED_SIEGE -> lanesToDisplay.addAll(listOf(Lane.RANGED, Lane.SIEGE))
            Lane.MELEE_SIEGE -> lanesToDisplay.addAll(listOf(Lane.MELEE, Lane.SIEGE))
        }

        return lanesToDisplay
    }

    fun getCards(filters: CardFilters, deckId: Int) {
        if (filters.filterByFactions.first) {
            val cursor = cardRepository.getAllFactionAndNeutralCards(filters.filterByFactions.second)
            val cards: MutableList<Card> = ArrayList()

            cursor.use {
                while (cursor.moveToNext()) {
                    cards.add(Card.MAPPER.apply(cursor))
                }
            }

            val sortedCards: List<Card> = cards.sortedBy { card -> card.name }

            if (deckId > 0) {
                val deck = deckRepository.getDeckById(deckId)
                if (deck != null && isViewAttached()) {
                    view!!.onDeckbuildModeCardsLoaded(sortedCards.filterNot { it.cardType == CardType.LEADER }, deck)
                }
            } else {
                getViewOrThrow().onViewModeCardsLoaded(sortedCards)
            }
        } else {
            val cards: List<Card> = cardRepository.getAllCards().sortedBy { card -> card.name }
            getViewOrThrow().onViewModeCardsLoaded(cards)
        }
    }

}

