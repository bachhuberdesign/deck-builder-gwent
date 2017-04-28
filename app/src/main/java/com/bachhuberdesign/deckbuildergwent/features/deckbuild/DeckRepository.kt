package com.bachhuberdesign.deckbuildergwent.features.deckbuild

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import com.google.gson.Gson
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.Normalizer
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckRepository @Inject constructor(var gson: Gson, val database: BriteDatabase) {

    companion object {
        @JvmStatic val TAG: String = DeckRepository::class.java.name
    }

    fun deckNameExists(deckName: String): Boolean {
        var normalizedDeckName = Normalizer.normalize(deckName, Normalizer.Form.NFD).replace("[^\\p{ASCII}]", "")
        normalizedDeckName = normalizedDeckName.replace("'", "''")

        val count = database.readableDatabase.compileStatement("SELECT COUNT(*) FROM ${Deck.TABLE} WHERE ${Deck.NAME} = '$normalizedDeckName';")

        return count.simpleQueryForLong() > 0
    }

    fun getDeckById(deckId: Int): Deck? {
        val deckCursor = database.query("SELECT * FROM ${Deck.TABLE} WHERE ${Deck.ID} = $deckId")
        var deck: Deck? = null

        deckCursor.use {
            while (deckCursor.moveToNext()) {
                deck = Deck.MAPPER.apply(deckCursor)
            }
        }

        deck?.cards = getCardsForDeck(deckId)

        return deck
    }

    fun observeCardUpdates(deckId: Int): Observable<MutableList<Card>> {
        val tables: MutableList<String> = arrayListOf(Card.TABLE, "user_decks_cards")
        val query: String = "SELECT * FROM ${Card.TABLE} " +
                "JOIN user_decks_cards as t2 " +
                "ON ${Card.ID} = t2.card_id " +
                "WHERE t2.deck_id = $deckId"

        return database.createQuery(tables, query)
                .mapToList(Card.MAP1)
                .skip(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCardsForDeck(deckId: Int): MutableList<Card> {
        val cursor = database.query("SELECT * FROM ${Card.TABLE} " +
                "JOIN user_decks_cards as t2 " +
                "ON ${Card.ID} = t2.card_id " +
                "WHERE t2.deck_id = $deckId")

        val cards: MutableList<Card> = ArrayList()

        cursor.use {
            while (cursor.moveToNext()) {
                cards.add(Card.MAPPER.apply(cursor))
            }
        }

        return cards
    }

    fun getAllUserCreatedDecks(): List<Deck> {
        val cursor = database.query("SELECT * FROM ${Deck.TABLE}")
        val decks: MutableList<Deck> = ArrayList()

        cursor.use {
            while (cursor.moveToNext()) {
                decks.add(Deck.MAPPER.apply(cursor))
            }
        }

        return decks
    }

    fun countUserDecksWithLeader(leaderCardId: Int): Int {
        val cursor = database.query("SELECT * FROM ${Deck.TABLE} " +
                "JOIN user_decks_cards as t2 " +
                "ON ${Deck.ID} = t2.deck_id " +
                "WHERE t2.card_id = $leaderCardId")

        cursor.use { cursor ->
            return cursor.count
        }
    }

    fun getFactions(): Cursor {
        return database.query("SELECT * FROM ${Faction.TABLE}")
    }

    fun getLeaders(): Cursor {
        return database.query("SELECT * FROM ${Card.TABLE} WHERE ${Card.TYPE} = ${CardType.LEADER}")
    }

    fun saveDeck(deck: Deck): Int {
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

        return deck.id
    }

    fun addCardToDeck(card: Card, deckId: Int) {
        val values = ContentValues()
        values.put("card_id", card.cardId)
        values.put("deck_id", deckId)

        if (card.selectedLane > 0) {
            values.put(Card.SELECTED_LANE, card.selectedLane)
        } else {
            values.put(Card.SELECTED_LANE, card.lane)
        }

        database.insert(Deck.JOIN_CARD_TABLE, values)
    }

    fun deleteCardFromDeck(card: Card, deckId: Int) {
        Log.i(TAG, "deleteCardFromDeck() cardId: ${card.cardId}, deckId: $deckId")

        database.delete(Deck.JOIN_CARD_TABLE,
                "join_id = " +
                        "(SELECT MIN(join_id) " +
                        "FROM user_decks_cards " +
                        "WHERE deck_id = $deckId " +
                        "AND card_id = ${card.cardId} " +
                        "AND ${Card.SELECTED_LANE} = ${card.selectedLane})")
    }

    fun deleteDeck(deckId: Int) {
        database.delete(Deck.JOIN_CARD_TABLE, "deck_id = $deckId")
        database.delete(Deck.TABLE, "${Deck.ID} = $deckId")
    }

    fun renameDeck(newDeckName: String, deckId: Int) {
        val values = ContentValues()
        values.put(Deck.NAME, newDeckName)

        database.update(Deck.TABLE, values, "${Deck.ID} = $deckId")
    }

}