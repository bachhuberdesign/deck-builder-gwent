package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckRepository {

    fun getUserCreatedDecks(): List<Deck> {
        val deckList: MutableList<Deck> = ArrayList()

        val newDeck = Deck(name = "New Deck")
        newDeck.cards.add(Card())

        deckList.add(newDeck)

        return deckList
    }

}