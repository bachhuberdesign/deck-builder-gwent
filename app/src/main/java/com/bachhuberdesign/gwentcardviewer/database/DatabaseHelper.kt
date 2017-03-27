package com.bachhuberdesign.gwentcardviewer.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        Log.d(TAG, "onCreate()")
        database.execSQL(CREATE_TABLE_CARDS)
        database.execSQL(CREATE_TABLE_USER_DECKS)
        database.execSQL(CREATE_TABLE_USER_DECKS_CARDS)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade()")
        // TODO:
    }

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
        const val DB_NAME = "deck_builder.db"
        const val DB_VERSION: Int = 1
        const val CREATE_TABLE_CARDS: String =
                "CREATE TABLE cards (" +
                        "${Card.ID} INTEGER NOT NULL PRIMARY KEY, " +
                        "${Card.NAME} TEXT NOT NULL, " +
                        "${Card.DESCRIPTION} TEXT NOT NULL, " +
                        "${Card.FLAVOR_TEXT} TEXT NOT NULL, " +
                        "${Card.ICON_URL} TEXT NOT NULL, " +
                        "${Card.MILL} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.MILL_PREMIUM} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.SCRAP} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.SCRAP_PREMIUM} INTEGER NOT NULL DEFAULT 0, " +
                        "${Card.FACTION} INTEGER NOT NULL, " +
                        "${Card.LANE} INTEGER NOT NULL, " +
                        "${Card.RARITY} INTEGER NOT NULL, " +
                        "${Card.TYPE} INTEGER NOT NULL" +
                        ")"
        const val CREATE_TABLE_USER_DECKS: String =
                "CREATE TABLE user_decks (" +
                        "${Deck.ID} INTEGER NOT NULL PRIMARY KEY, " +
                        "${Deck.NAME} TEXT NOT NULL, " +
                        "${Deck.FACTION} INTEGER NOT NULL, " +
                        "${Deck.FAVORITED} INTEGER NOT NULL DEFAULT 0, " +
                        "${Deck.CREATED_DATE} INTEGER NOT NULL, " +
                        "${Deck.LAST_UPDATE} INTEGER NOT NULL" +
                        ")"
        const val CREATE_TABLE_USER_DECKS_CARDS: String =
                "CREATE TABLE user_decks_cards (" +
                        "id INTEGER NOT NULL PRIMARY KEY, " +
                        "deck_id INTEGER NOT NULL, " +
                        "card_id INTEGER NOT NULL" +
                        ")"
    }

}
