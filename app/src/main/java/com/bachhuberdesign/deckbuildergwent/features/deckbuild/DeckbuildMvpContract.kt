package com.bachhuberdesign.deckbuildergwent.features.deckbuild

import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckbuildMvpContract : MvpContract {

    fun onDeckDeleted(deckId: Int)

    fun onDeckLoaded(deck: Deck, leader: Card)

    fun onErrorLoadingDeck(message: String)

    fun showCardsByLane(cards: List<Card>, lane: Int)

    fun animateCards(cardsToAnimate: List<Card>)

    fun deckTotalsUpdated(totals: LaneTotals)

    fun showDeckRenameDialog(currentDeckName: String)

    fun onDeckRenamed(newDeckName: String)

}