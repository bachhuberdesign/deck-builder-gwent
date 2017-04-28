package com.bachhuberdesign.deckbuildergwent.features.deckselect

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckSelectMvpContract : MvpContract {

    fun onDecksLoaded(decks: List<Deck>)

    fun onNoDecksAvailable()

}