package com.bachhuberdesign.deckbuildergwent.features.deckselect

import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardRepository
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
class DeckSelectPresenter
@Inject constructor(private val deckRepository: DeckRepository,
                    private val cardRepository: CardRepository) : BasePresenter<DeckSelectMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckSelectPresenter::class.java.name
    }

    fun loadDeckList() {
        val decks = deckRepository.getAllUserCreatedDecks()

        decks.forEach { deck ->
            deck.leader = cardRepository.getCardById(deck.leaderId)
        }

        if (decks.isNotEmpty()) {
            getViewOrThrow().onDecksLoaded(decks)
        } else {
            getViewOrThrow().onNoDecksAvailable()
        }
    }

}