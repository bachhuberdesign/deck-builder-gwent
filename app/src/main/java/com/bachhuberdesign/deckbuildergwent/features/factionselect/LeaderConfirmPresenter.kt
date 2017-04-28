package com.bachhuberdesign.deckbuildergwent.features.factionselect

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import java.util.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class LeaderConfirmPresenter
@Inject constructor(private val repository: DeckRepository) : BasePresenter<LeaderConfirmMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = LeaderConfirmPresenter::class.java.name
    }

    fun saveNewDeck(deckName: String, leader: Card) {
        if (isDeckNameValid(deckName)) {
            val deck: Deck = Deck(
                    name = deckName,
                    faction = leader.faction,
                    leader = leader.cardId,
                    createdDate = Calendar.getInstance().time)

            val deckId = repository.saveDeck(deck)

            if (isViewAttached()) {
                view!!.onDeckSaved(deckId)
            }
        }
    }

    fun loadDefaultDeckName(leader: Card) {
        val count = repository.countUserDecksWithLeader(leader.cardId)

        if (isViewAttached()) {
            view!!.onDefaultDeckNameLoaded("${leader.name} Deck #${count + 1}")
        }
    }

    private fun isDeckNameValid(deckName: String): Boolean {
        if (deckName.isNullOrEmpty()) {
            view!!.displayError("Please enter a name for your deck.")
            return false
        } else if (repository.deckNameExists(deckName) && isViewAttached()) {
            view!!.displayError("A deck with that name already exists, please try again.")
            return false
        }
        return true
    }

}