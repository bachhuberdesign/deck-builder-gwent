package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckRepository
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckCardListPresenter
@Inject constructor(private val deckRepository: DeckRepository) : BasePresenter<DeckCardListMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckCardListPresenter::class.java.name
    }

}