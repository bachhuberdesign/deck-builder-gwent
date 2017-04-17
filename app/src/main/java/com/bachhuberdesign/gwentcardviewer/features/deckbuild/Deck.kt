package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.database.Cursor
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.CardType
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
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
        const val ID = "_id"
        const val NAME = "name"
        const val FACTION = "faction"
        const val FAVORITED = "favorited"
        const val CREATED_DATE = "created_date"
        const val LAST_UPDATE = "last_update"
        const val MAX_NUM_CARDS = 40

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

        fun isCardAddableToDeck(deck: Deck, card: Card): Boolean {
            // TODO: Check if too many of card are already in deck
            // https://www.playgwent.com/en/faq

            // Check that deck is not full
            if (deck.cards.size >= MAX_NUM_CARDS) {
                return false
            }

            // If leader card is being added, check that no leader already exists in deck
            if (card.cardType == CardType.LEADER) {
                deck.cards.forEach {
                    if (card.cardType == CardType.LEADER) {
                        return false
                    }
                }
            }

            // Check that card is neutral or same faction as deck
            if (card.faction != Faction.NEUTRAL) {
                if (deck.faction != card.faction) {
                    return false
                }
            }

            // TODO: Add additional validation checks

            return true
        }
    }

}