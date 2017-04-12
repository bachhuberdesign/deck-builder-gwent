package com.bachhuberdesign.gwentcardviewer.features.deckselect

import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckRepository
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckbuildController
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckSelectPresenter
@Inject constructor(private val deckRepository: DeckRepository) : BasePresenter<DeckSelectMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckSelectPresenter::class.java.name
    }

    fun loadDeckList() {

    }

}