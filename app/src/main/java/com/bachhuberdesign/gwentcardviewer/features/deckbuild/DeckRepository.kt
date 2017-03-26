package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.CardType
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import com.google.gson.Gson
import com.squareup.sqlbrite.BriteDatabase
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckRepository @Inject constructor(var gson: Gson, val database: BriteDatabase) {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    fun getUserCreatedDecks(): List<Deck> {
        val deckList: MutableList<Deck> = ArrayList()

        val newDeck = Deck(name = "New Deck")
        newDeck.cards.add(Card(cardType = CardType.LEADER))

        deckList.add(newDeck)

        return deckList
    }

    fun removeDeck(deckId: Int) {
        TODO("Not yet implemented.")
    }

}