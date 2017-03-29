package com.bachhuberdesign.gwentcardviewer.features.factionselect

import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class FactionSelectPresenter : BasePresenter<FactionSelectMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    override fun attach(view: FactionSelectMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    fun loadFactions(){

    }


}