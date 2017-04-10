package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.database.Cursor
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import com.squareup.sqlbrite.BriteDatabase
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class CardRepository @Inject constructor(val database: BriteDatabase) {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    fun getCardById(cardId: Int): Card {
        val cursor = database.query("SELECT * FROM ${Card.TABLE} WHERE ${Card.ID} =  $cardId")
        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                return Card.MAPPER.apply(cursor)
            }
        }

        throw CardException("Card $cardId not found.")
    }

    fun getAllCards(): List<Card> {
        val cursor = database.query("SELECT * FROM ${Card.TABLE}")
        val cards: MutableList<Card> = ArrayList()

        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                cards.add(Card.MAPPER.apply(cursor))
            }
        }

        if (cards.isEmpty()) {
            throw CardException("Problem loading cards from database.")
        } else {
            return cards
        }
    }

    fun getAllFactionAndNeutralCards(factionId: Int): Cursor {
        return database.query("SELECT * FROM ${Card.TABLE} " +
                "WHERE ${Card.FACTION} = $factionId " +
                "OR ${Card.FACTION} = ${Faction.NEUTRAL}")
    }

}