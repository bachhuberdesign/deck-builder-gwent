package com.bachhuberdesign.deckbuildergwent.features.deckbuild

import android.content.ContentValues
import com.bachhuberdesign.deckbuildergwent.features.shared.exception.CardException
import com.bachhuberdesign.deckbuildergwent.features.shared.exception.DeckException
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.features.stattrack.Match
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
 * Helper class which contains functions that pertain to SQLite [Deck] queries and persistence.
 *
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckRepository
@Inject constructor(var gson: Gson, val database: BriteDatabase) {

    companion object {
        @JvmStatic val TAG: String = DeckRepository::class.java.name
    }

    /**
     * Checks if a given [deckName] has been persisted in the database.
     *
     * @return True if a deck with [deckName] is persisted. False if no deck with [deckName] is persisted.
     */
    fun deckNameExists(deckName: String): Boolean {
        var normalizedDeckName = Normalizer.normalize(deckName, Normalizer.Form.NFD).replace("[^\\p{ASCII}]", "")
        normalizedDeckName = normalizedDeckName.replace("'", "''")

        val count = database.readableDatabase.compileStatement("SELECT COUNT(*) FROM ${Deck.TABLE} WHERE ${Deck.NAME} = '$normalizedDeckName';")

        return count.simpleQueryForLong() > 0
    }

    /**
     * @return [Deck]
     */
    fun getDeckById(deckId: Int): Deck? {
        if (deckId <= 0) {
            throw DeckException("Expected a valid deck id but received $deckId.")
        }

        val deckCursor = database.query("SELECT * FROM ${Deck.TABLE} WHERE ${Deck.ID} = $deckId")
        var deck: Deck? = null

        deckCursor.use {
            while (deckCursor.moveToNext()) {
                deck = Deck.MAPPER.apply(deckCursor)
            }
        }

        if (deck == null) {
            throw DeckException("Unable to load deck $deckId")
        } else {
            deck!!.leader = getLeaderCardForDeck(deck!!.leaderId)
            deck!!.cards = getCardsForDeck(deckId)
        }

        return deck
    }

    /**
     *
     * @return [Deck]
     */
    fun getMostRecentDeck(): Deck? {
        val cursor = database.query("SELECT * FROM ${Deck.TABLE} " +
                "ORDER BY last_update DESC " +
                "LIMIT 1")

        cursor.use { cursor ->
            if (cursor.moveToNext()) {
                return Deck.MAPPER.apply(cursor)
            } else {
                return null
            }
        }
    }

    /**
     *
     */
    fun observeRecentlyUpdatedDecks(): Observable<MutableList<Deck>> {
        val query = "SELECT * FROM ${Deck.TABLE} " +
                "ORDER BY last_update DESC " +
                "LIMIT 5"
        return database.createQuery(Deck.TABLE, query)
                .mapToList(Deck.MAP1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     * @return [Card]
     */
    private fun getLeaderCardForDeck(leaderCardId: Int): Card {
        val cursor = database.query("SELECT * FROM ${Card.TABLE} WHERE ${Card.ID} =  $leaderCardId")

        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                return Card.MAPPER.apply(cursor)
            }
        }

        throw CardException("Leader $leaderCardId not found.")
    }

    /**
     * @return
     */
    fun getAllUserCreatedDecks(): List<Deck> {
        val cursor = database.query("SELECT * FROM ${Deck.TABLE}")
        val decks: MutableList<Deck> = ArrayList()

        cursor.use {
            while (cursor.moveToNext()) {
                decks.add(Deck.MAPPER.apply(cursor))
            }
        }

        decks.forEach { deck ->
            deck.leader = getLeaderCardForDeck(deck.id)
        }

        return decks
    }

    /**
     * @return [Int]
     */
    fun countUserDecksWithLeader(leaderCardId: Int): Int {
        val cursor = database.query("SELECT * FROM ${Deck.TABLE} " +
                "WHERE ${Deck.LEADER_ID} = $leaderCardId")

        cursor.use { cursor ->
            return cursor.count
        }
    }

    /**
     *
     */
    fun getFactions(): List<Faction> {
        val factions: MutableList<Faction> = ArrayList()
        val factionsCursor = database.query("SELECT * FROM ${Faction.TABLE}")

        factionsCursor.use {
            while (factionsCursor.moveToNext()) {
                factions.add(Faction.MAPPER.apply(factionsCursor))
            }
        }

        factions.forEach { faction ->
            faction.leaders = getLeadersForFaction(faction.id) as MutableList<Card>
        }

        return factions
    }

    /**
     *
     */
    fun getLeadersForFaction(factionId: Int): List<Card> {
        val cursor = database.query("SELECT * FROM ${Card.TABLE} " +
                "WHERE ${Card.TYPE} = ${CardType.LEADER} " +
                "AND ${Card.FACTION} = $factionId")
        val leaders: MutableList<Card> = ArrayList()

        cursor.use {
            while (cursor.moveToNext()) {
                leaders.add(Card.MAPPER.apply(cursor))
            }
        }

        return leaders
    }

    /**
     *
     */
    fun saveDeck(deck: Deck): Int {
        val currentTime = Date().time
        val deckValues = ContentValues()

        deckValues.put(Deck.NAME, deck.name)
        deckValues.put(Deck.FACTION, deck.faction)
        deckValues.put(Deck.LEADER_ID, deck.leader!!.cardId)
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

    /**
     *
     */
    fun updateLeaderForDeck(deckId: Int, leaderId: Int) {
        val deckValues = ContentValues()
        deckValues.put(Deck.LEADER_ID, leaderId)

        database.update(Deck.TABLE, deckValues, "${Deck.ID} = $deckId")
    }

    /**
     *
     */
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

        saveDeckLastUpdateTime(deckId)
    }

    /**
     *
     */
    fun renameDeck(newDeckName: String, deckId: Int) {
        val values = ContentValues()
        values.put(Deck.NAME, newDeckName)

        database.update(Deck.TABLE, values, "${Deck.ID} = $deckId")
    }

    /**
     *
     */
    fun removeCardFromDeck(card: Card, deckId: Int) {
        val query: String

        if (card.selectedLane == 0) {
            query = "join_id = " +
                    "(SELECT MIN(join_id) " +
                    "FROM user_decks_cards " +
                    "WHERE deck_id = $deckId " +
                    "AND card_id = ${card.cardId})"
        } else {
            query = "join_id = " +
                    "(SELECT MIN(join_id) " +
                    "FROM user_decks_cards " +
                    "WHERE deck_id = $deckId " +
                    "AND card_id = ${card.cardId} " +
                    "AND ${Card.SELECTED_LANE} = ${card.selectedLane})"
        }

        database.delete(Deck.JOIN_CARD_TABLE, query)

        saveDeckLastUpdateTime(deckId)
    }

    /**
     *
     */
    fun deleteDeck(deckId: Int) {
        database.delete(Deck.JOIN_CARD_TABLE, "deck_id = $deckId")
        database.delete(Deck.TABLE, "${Deck.ID} = $deckId")
        database.delete(Match.TABLE, "${Match.DECK_ID} = $deckId")
    }

    private fun saveDeckLastUpdateTime(deckId: Int) {
        val deckValues = ContentValues()
        deckValues.put(Deck.LAST_UPDATE, Date().time)

        database.update(Deck.TABLE, deckValues, "${Deck.ID} = $deckId")
    }

}