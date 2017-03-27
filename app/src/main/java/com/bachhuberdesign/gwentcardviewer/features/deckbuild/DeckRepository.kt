package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import com.google.gson.Gson
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.QueryObservable
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

    fun addDeck(deck: Deck) {

    }

    fun queryUserCreatedDecks(): QueryObservable {
        val userDecks: MutableList<Deck> = ArrayList()

        return database.createQuery(Deck.TABLE, "SELECT * FROM ${Deck.TABLE}")
    }

    fun removeDeck(deckId: Int) {
        TODO("Not yet implemented.")
    }

}