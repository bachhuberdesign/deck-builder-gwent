package com.bachhuberdesign.gwentcardviewer.features.factionselect

import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface FactionSelectMvpContract : MvpContract {

    fun onFactionsLoaded()

    fun onFactionSelected()

    fun onLeaderSelected()

    fun onLeaderConfirmed()

}