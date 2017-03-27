package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.content.ContentValues
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import com.google.gson.Gson
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.QueryObservable
import java.util.*
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

    fun saveDeck(deck: Deck) {
        val currentTime = Date().time
        val deckValues = ContentValues()

        deckValues.put(Deck.NAME, deck.name)
        deckValues.put(Deck.FACTION, deck.faction)
        deckValues.put(Deck.FAVORITED, deck.isFavorited)
        deckValues.put(Deck.LAST_UPDATE, currentTime)

        if (deck.id == 0) {
            deckValues.put(Deck.CREATED_DATE, currentTime)

            deck.id = database.insert(Deck.TABLE, deckValues).toInt()
        } else {
            database.update(Deck.TABLE, deckValues, "${Deck.ID} = ${deck.id}")
        }

        database.delete(Deck.JOIN_CARD_TABLE, "deck_id = ${deck.id}")
        deck.cards.forEach { card ->
            val cardValues = ContentValues()
            cardValues.put("card_id", card.cardId)
            cardValues.put("deck_id", deck.id)
            database.insert(Deck.JOIN_CARD_TABLE, cardValues)
        }
    }

    fun queryUserCreatedDecks(): QueryObservable {
        return database.createQuery(Deck.TABLE, "SELECT * FROM ${Deck.TABLE}")
    }

    fun removeDeck(deckId: Int) {
        TODO("Not yet implemented.")
    }

}