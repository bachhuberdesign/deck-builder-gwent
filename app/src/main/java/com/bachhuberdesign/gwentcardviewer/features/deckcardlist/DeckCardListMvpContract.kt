package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckCardListMvpContract : MvpContract {

    fun onDeckLoaded(deck: Deck)

    fun onCardRemoved(cardId: Int)

    fun showScrapCount(scrapCount: Int)

    fun showErrorMessage(message: String)

}