package com.bachhuberdesign.deckbuildergwent.features.stattrack.addmatchdialog

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface AddMatchDialogMvpContract : MvpContract {

    fun onDecksLoaded(decks: List<Deck>)

    fun onFactionsLoaded(factions: List<Faction>)

}