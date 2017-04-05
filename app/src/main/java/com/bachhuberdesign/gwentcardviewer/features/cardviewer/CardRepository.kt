package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.database.Cursor
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import com.squareup.sqlbrite.BriteDatabase
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckRepository @Inject constructor(val database: BriteDatabase) {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    fun getAllCards(): Cursor {
        return database.query("SELECT * FROM ${Card.TABLE}")
    }

    fun getAllFactionAndNeutralCards(factionId: Int): Cursor {
        return database.query("SELECT * FROM ${Card.TABLE} WHERE ${Card.FACTION} = $factionId")
    }

}