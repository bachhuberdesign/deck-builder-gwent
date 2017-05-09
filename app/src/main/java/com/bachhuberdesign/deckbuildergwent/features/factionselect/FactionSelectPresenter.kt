package com.bachhuberdesign.deckbuildergwent.features.factionselect

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class FactionSelectPresenter
@Inject constructor(private val repository: DeckRepository) : BasePresenter<FactionSelectMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = FactionSelectPresenter::class.java.name
    }

    /**
     *
     */
    fun loadFactions() {
        val factions: List<Faction> = repository.getFactions()

        getViewOrThrow().onFactionsLoaded(factions)
    }


}