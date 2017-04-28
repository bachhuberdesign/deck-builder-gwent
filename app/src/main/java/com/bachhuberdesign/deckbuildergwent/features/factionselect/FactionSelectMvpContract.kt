package com.bachhuberdesign.deckbuildergwent.features.factionselect

import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface FactionSelectMvpContract : MvpContract {

    fun onFactionsLoaded(factions: List<Faction>)

    fun onLeaderSelected(leader: Card)

}