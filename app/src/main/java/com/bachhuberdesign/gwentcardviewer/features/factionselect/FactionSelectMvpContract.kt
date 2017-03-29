package com.bachhuberdesign.gwentcardviewer.features.factionselect

import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface FactionSelectMvpContract : MvpContract {

    fun onFactionsLoaded(factions: List<Faction>)

    fun onFactionSelected(factionId: Int)

    fun onLeaderSelected(cardId: Int)

    fun onLeaderConfirmed()

}