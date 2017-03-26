package com.bachhuberdesign.gwentcardviewer.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // TODO:
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO:
    }

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
        const val DB_NAME = "deck_builder.db"
        const val DB_VERSION: Int = 1
        const val CREATE_CARDS_TABLE: String =
                "CREATE TABLE cards (" +
                        "{${Card.ID} INTEGER NOT NULL PRIMARY KEY " +
                        "${Card.NAME} TEXT NOT NULL " +
                        "${Card.DESCRIPTION} TEXT NOT NULL " +
                        "${Card.FLAVOR_TEXT} TEXT NOT NULL " +
                        "${Card.ICON_URL} TEXT NOT NULL " +
                        "${Card.MILL} INTEGER NOT NULL DEFAULT 0 " +
                        "${Card.MILL_PREMIUM} INTEGER NOT NULL DEFAULT 0 " +
                        "${Card.SCRAP} INTEGER NOT NULL DEFAULT 0 " +
                        "${Card.SCRAP_PREMIUM} INTEGER NOT NULL DEFAULT 0 " +
                        "${Card.FACTION} INTEGER NOT NULL " +
                        "${Card.LANE} INTEGER NOT NULL " +
                        "${Card.RARITY} INTEGER NOT NULL " +
                        "${Card.TYPE} INTEGER NOT NULL" +
                        ")"
    }

}
