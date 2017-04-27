package com.bachhuberdesign.gwentcardviewer

import com.bachhuberdesign.MainMvpContract
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
class MainPresenter
@Inject constructor(val deckRepository: DeckRepository) : BasePresenter<MainMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = MainPresenter::class.java.name
    }

    override fun attach(view: MainMvpContract) {
        super.attach(view)
        // TODO: Sub to deck updates
    }

    override fun detach() {
        super.detach()
        // TODO: Unsub
    }
}