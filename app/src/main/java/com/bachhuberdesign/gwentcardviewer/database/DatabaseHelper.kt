package com.bachhuberdesign.gwentcardviewer.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.google.gson.Gson

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DatabaseHelper(var context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        @JvmStatic val TAG: String = DatabaseHelper::class.java.name
        const val DB_NAME = "deck_builder.db"
        const val DB_VERSION: Int = 1
        const val CREATE_TABLE_CARDS: String =
                "CREATE TABLE ${Card.TABLE} (" +
                        "${Card.ID} INTEGER NOT NULL PRIMARY KEY, " +
                        "${Card.NAME} TEXT NOT NULL, " +
                        "${Card.DESCRIPTION} TEXT NOT NULL, " +
                        "${Card.FLAVOR_TEXT} TEXT NOT NULL, " +
                        "${Card.ICON_URL} TEXT NOT NULL, " +
                        "${Card.MILL} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.MILL_PREMIUM} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.SCRAP} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.SCRAP_PREMIUM} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.POWER} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.FACTION} INTEGER NOT NULL, " +
                        "${Card.LANE} INTEGER NOT NULL, " +
                        "${Card.LOYALTY} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.RARITY} INTEGER NOT NULL, " +
                        "${Card.TYPE} INTEGER NOT NULL" +
                        ")"
        const val CREATE_TABLE_USER_DECKS: String =
                "CREATE TABLE ${Deck.TABLE} (" +
                        "${Deck.ID} INTEGER NOT NULL PRIMARY KEY, " +
                        "${Deck.NAME} TEXT NOT NULL, " +
                        "${Deck.FACTION} INTEGER NOT NULL, " +
                        "${Deck.FAVORITED} INTEGER NOT NULL DEFAULT 0, " +
                        "${Deck.CREATED_DATE} INTEGER NOT NULL, " +
                        "${Deck.LAST_UPDATE} INTEGER NOT NULL" +
                        ")"
        const val CREATE_TABLE_USER_DECKS_CARDS: String =
                "CREATE TABLE ${Deck.JOIN_CARD_TABLE} ( " +
                        "deck_id INTEGER NOT NULL, " +
                        "card_id INTEGER NOT NULL, " +
                        "${Card.SELECTED_LANE} INTEGER NOT NULL" +
                        ")"
        const val CREATE_TABLE_FACTIONS: String =
                "CREATE TABLE ${Faction.TABLE} (" +
                        "${Faction.ID} INTEGER NOT NULL PRIMARY KEY, " +
                        "${Faction.NAME} TEXT NOT NULL, " +
                        "${Faction.EFFECT} TEXT NOT NULL," +
                        "${Faction.ICON_URL} TEXT NOT NULL" +
                        ")"
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_TABLE_CARDS)
        database.execSQL(CREATE_TABLE_USER_DECKS)
        database.execSQL(CREATE_TABLE_USER_DECKS_CARDS)
        database.execSQL(CREATE_TABLE_FACTIONS)

        val cards = Gson().fromJson(loadJsonFromAssets("card_list.json"), Array<Card>::class.java)
        val factions = Gson().fromJson(loadJsonFromAssets("faction_list.json"), Array<Faction>::class.java)

        cards.forEach { (id, name, description, flavorText, iconUrl, mill, millPremium, scrap,
                                scrapPremium, power, faction, lane, loyalty, rarity, cardType) ->
            val cv = ContentValues()
            cv.put(Card.NAME, name)
            cv.put(Card.DESCRIPTION, description)
            cv.put(Card.FLAVOR_TEXT, flavorText)
            cv.put(Card.ICON_URL, iconUrl)
            cv.put(Card.MILL, mill)
            cv.put(Card.MILL_PREMIUM, millPremium)
            cv.put(Card.SCRAP, scrap)
            cv.put(Card.SCRAP_PREMIUM, scrapPremium)
            cv.put(Card.POWER, power)
            cv.put(Card.FACTION, faction)
            cv.put(Card.LANE, lane)
            cv.put(Card.LOYALTY, loyalty)
            cv.put(Card.RARITY, rarity)
            cv.put(Card.TYPE, cardType)
            database.insertWithOnConflict(Card.TABLE, null, cv, CONFLICT_REPLACE)
        }

        factions.forEach { (id, name, effect, iconUrl) ->
            val cv = ContentValues()
            cv.put(Faction.ID, id)
            cv.put(Faction.NAME, name)
            cv.put(Faction.EFFECT, effect)
            cv.put(Faction.ICON_URL, iconUrl)
            database.insertWithOnConflict(Faction.TABLE, null, cv, CONFLICT_REPLACE)
        }
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade()")
    }

    private fun loadJsonFromAssets(fileName: String): String {
        val stream = context.assets.open(fileName)
        val buffer = ByteArray(stream.available())
        stream.use { stream ->
            stream.read(buffer)
        }

        return String(buffer).format("UTF-8")
    }

}
