package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckbuildMvpContract : MvpContract {

    fun onCardAdded()

    fun onCardRemoved()

    fun onDecksLoaded(decks: List<Deck>)

    fun onDeckDeleted(deckId: Int)

}