package com.bachhuberdesign

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface MainMvpContract : MvpContract {

    fun showRecentDecksInDrawer(decks: List<Deck>)

}