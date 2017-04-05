package com.bachhuberdesign.gwentcardviewer.features.factionselect

import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckRepository
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
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
        @JvmStatic val TAG: String = this::class.java.name
    }

    fun saveNewDeck(deckName: String, leader: Card) {
        if (deckName.isNullOrEmpty()) {
            view!!.displayError("Please enter a name for your deck.")
        } else if (repository.deckNameExists(deckName) && isViewAttached()) {
            view!!.displayError("A deck with that name already exists, please try again.")
        } else {
            val deck: Deck = Deck(
                    name = deckName,
                    faction = leader.faction,
                    createdDate = Calendar.getInstance().time)
            deck.cards.add(leader)

            val deckId = repository.saveDeck(deck)

            if (isViewAttached()) {
                view!!.onDeckSaved(deckId)
            }
        }
    }

}