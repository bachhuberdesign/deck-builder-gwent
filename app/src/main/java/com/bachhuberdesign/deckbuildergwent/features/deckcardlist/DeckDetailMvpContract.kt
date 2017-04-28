package com.bachhuberdesign.deckbuildergwent.features.deckcardlist

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckDetailMvpContract : MvpContract {

    fun onDeckLoaded(deck: Deck, leader: Card)

    fun showErrorMessage(message: String)

}