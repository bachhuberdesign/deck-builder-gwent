package com.bachhuberdesign.deckbuildergwent.features.deckdetail

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface DeckDetailMvpContract : MvpContract {

    fun onDeckLoaded(deck: Deck)

    fun onLeadersLoaded(leaders: List<Card>)

    fun showErrorMessage(message: String)

    fun showDeckNameChangeDialog()

}