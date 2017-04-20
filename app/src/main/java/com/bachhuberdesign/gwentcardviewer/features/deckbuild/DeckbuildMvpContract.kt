package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckbuildMvpContract : MvpContract {

    fun onCardAdded(card: Card)

    fun onCardRemoved(card: Card)

    fun onDeckDeleted(deckId: Int)

    fun onDeckLoaded(deck: Deck)

    fun onErrorLoadingDeck(message: String)

    fun showCardsByLane(cards: List<Card>, lane: Int)

    fun animateCards(cardsToAnimate: List<Card>)

    fun deckTotalsUpdated(totals: LaneTotals)

}