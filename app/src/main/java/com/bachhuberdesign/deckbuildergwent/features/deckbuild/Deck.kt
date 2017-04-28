package com.bachhuberdesign.deckbuildergwent.features.deckbuild

import android.database.Cursor
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.util.getBooleanFromColumn
import com.bachhuberdesign.deckbuildergwent.util.getIntFromColumn
import com.bachhuberdesign.deckbuildergwent.util.getLongFromColumn
import com.bachhuberdesign.deckbuildergwent.util.getStringFromColumn
import io.reactivex.functions.Function
import rx.functions.Func1
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
                var leader: Int = 0,
                var cards: MutableList<Card> = ArrayList(),
                var isFavorited: Boolean = false,
                var createdDate: Date = Date(),
                var lastUpdate: Date? = Date()) {

    companion object {
        const val TABLE = "user_decks"
        const val JOIN_CARD_TABLE = "user_decks_cards"
        const val ID = "_id"
        const val NAME = "name"
        const val LEADER = "leader"
        const val FACTION = "faction"
        const val FAVORITED = "favorited"
        const val CREATED_DATE = "created_date"
        const val LAST_UPDATE = "last_update"
        const val MAX_NUM_CARDS = 40

        val MAP1 = Func1<Cursor, Deck> { cursor ->
            val deck = Deck()
            deck.id = cursor.getIntFromColumn(Deck.ID)
            deck.name = cursor.getStringFromColumn(Deck.NAME)
            deck.faction = cursor.getIntFromColumn(Deck.FACTION)
            deck.leader = cursor.getIntFromColumn(Deck.LEADER)
            deck.isFavorited = cursor.getBooleanFromColumn(Deck.FAVORITED)
            deck.createdDate = Date(cursor.getLongFromColumn(Deck.CREATED_DATE))
            deck.lastUpdate = Date(cursor.getLongFromColumn(Deck.LAST_UPDATE))
            deck
        }

        val MAPPER = Function<Cursor, Deck> { cursor ->
            val deck = Deck()
            deck.id = cursor.getIntFromColumn(Deck.ID)
            deck.name = cursor.getStringFromColumn(Deck.NAME)
            deck.faction = cursor.getIntFromColumn(Deck.FACTION)
            deck.leader = cursor.getIntFromColumn(Deck.LEADER)
            deck.isFavorited = cursor.getBooleanFromColumn(Deck.FAVORITED)
            deck.createdDate = Date(cursor.getLongFromColumn(Deck.CREATED_DATE))
            deck.lastUpdate = Date(cursor.getLongFromColumn(Deck.LAST_UPDATE))
            deck
        }

        fun isCardAddableToDeck(deck: Deck, card: Card): Boolean {
            // Check that deck is not full
            if (deck.cards.size >= MAX_NUM_CARDS) {
                return false
            }

            // Check that card is neutral or same faction as deck
            if (card.faction != Faction.NEUTRAL) {
                if (deck.faction != card.faction) {
                    return false
                }
            }

            val filteredByCardType = deck.cards.filter {
                it.cardType == card.cardType
            }

            val filteredById = deck.cards.filter {
                it.cardId == card.cardId
            }

            if (card.cardType == CardType.LEADER) return false

            if (card.cardType == CardType.BRONZE && filteredById.size >= 3) return false

            if (card.cardType == CardType.SILVER && filteredById.isNotEmpty()) return false
            if (card.cardType == CardType.SILVER && filteredByCardType.size >= 6) return false

            if (card.cardType == CardType.GOLD && filteredById.isNotEmpty()) return false
            if (card.cardType == CardType.GOLD && filteredByCardType.size >= 4) return false

            return true
        }
    }

}