package com.bachhuberdesign.deckbuildergwent.features.factionselect

import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface LeaderConfirmMvpContract : MvpContract {

    fun onDeckSaved(deckId: Int)

    fun displayError(messageToDisplay: String)

    fun onDefaultDeckNameLoaded(deckName: String)

}