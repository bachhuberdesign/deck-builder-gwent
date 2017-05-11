package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface CardViewerMvpContract : MvpContract {

    fun onViewModeCardsLoaded(cards: List<Card>)

    fun onDeckbuildModeCardsLoaded(cards: List<Card>, deck: Deck)

    fun onListFiltered(filteredCards: List<Card>)

    fun onCardChecked(card: Card, isCardAddable: Boolean)

    fun showLaneSelection(lanesToDisplay: List<Int>, card: Card)

    fun updateCount(card: Card, itemRemoved: Boolean)

}