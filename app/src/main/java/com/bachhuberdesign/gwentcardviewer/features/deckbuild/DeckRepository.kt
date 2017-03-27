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
        val values = ContentValues()

        values.put(Deck.NAME, deck.name)
        values.put(Deck.FACTION, deck.faction)
        values.put(Deck.FAVORITED, deck.isFavorited)
        values.put(Deck.LAST_UPDATE, currentTime)

        if (deck.id == 0) {
            values.put(Deck.CREATED_DATE, currentTime)

            database.insert(Deck.TABLE, values)
        } else {
            database.update(Deck.TABLE, values, "${Deck.ID} = ${deck.id}")
        }
    }

    fun queryUserCreatedDecks(): QueryObservable {
        return database.createQuery(Deck.TABLE, "SELECT * FROM ${Deck.TABLE}")
    }

    fun removeDeck(deckId: Int) {
        TODO("Not yet implemented.")
    }

}