package com.bachhuberdesign.deckbuildergwent.features.stattrack

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class AddMatchDialogPresenter
@Inject constructor(private val deckRepository: DeckRepository) : BasePresenter<AddMatchDialogMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = AddMatchDialogPresenter::class.java.name
    }

    /**
     *
     */
    fun loadDecks() {
        val decks = deckRepository.getAllUserCreatedDecks()

        if (decks.isNotEmpty()) {
            getViewOrThrow().onDecksLoaded(decks)
        }
    }

    /**
     *
     */
    fun loadFactions() {
        val factions = deckRepository.getFactions()

        if (factions.isNotEmpty()) {
            getViewOrThrow().onFactionsLoaded(factions)
        }
    }

}