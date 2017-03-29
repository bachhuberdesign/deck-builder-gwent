package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.database.Cursor
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.util.getBooleanFromColumn
import com.bachhuberdesign.gwentcardviewer.util.getIntFromColumn
import com.bachhuberdesign.gwentcardviewer.util.getLongFromColumn
import com.bachhuberdesign.gwentcardviewer.util.getStringFromColumn
import io.reactivex.functions.Function
import java.util.Date
import kotlin.collections.ArrayList

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class Deck(var id: Int = 0,
                var name: String = "",
                var faction: Int = 0,
                var cards: MutableList<Card> = ArrayList(),
                var isFavorited: Boolean = false,
                var createdDate: Date = Date(),
                var lastUpdate: Date? = Date()) {

    companion object {
        const val TABLE = "user_decks"
        const val JOIN_CARD_TABLE = "user_decks_cards"
        const val ID = "id"
        const val NAME = "name"
        const val FACTION = "faction"
        const val FAVORITED = "favorited"
        const val CREATED_DATE = "created_date"
        const val LAST_UPDATE = "last_update"

        val MAPPER = Function<Cursor, Deck> { cursor ->
            val deck = Deck()
            deck.id = cursor.getIntFromColumn(Deck.ID)
            deck.name = cursor.getStringFromColumn(Deck.NAME)
            deck.faction = cursor.getIntFromColumn(Deck.FACTION)
            deck.isFavorited = cursor.getBooleanFromColumn(Deck.FAVORITED)
            deck.createdDate = Date(cursor.getLongFromColumn(Deck.CREATED_DATE))
            deck.lastUpdate = Date(cursor.getLongFromColumn(Deck.LAST_UPDATE))
            deck
        }
    }

}