package com.bachhuberdesign.gwentcardviewer.features.deckselect

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckSelectMvpContract : MvpContract {

    fun onDecksLoaded(decks: List<Deck>)

}