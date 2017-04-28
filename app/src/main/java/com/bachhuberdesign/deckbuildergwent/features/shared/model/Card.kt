package com.bachhuberdesign.deckbuildergwent.features.shared.model

import android.database.Cursor
import com.bachhuberdesign.deckbuildergwent.util.getIntFromColumn
import com.bachhuberdesign.deckbuildergwent.util.getStringFromColumn
import io.reactivex.functions.Function
import rx.functions.Func1

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class Card(var cardId: Int = 0,
                var name: String = "",
                var description: String = "",
                var flavorText: String = "",
                var iconUrl: String = "",
                var mill: Int = 0,
                var millPremium: Int = 0,
                var scrap: Int = 0,
                var scrapPremium: Int = 0,
                var power: Int = 0,
                var faction: Int = 0,
                var lane: Int = 0,
                var loyalty: Int = 0,
                var rarity: Int = 0,
                var cardType: Int = 0,
                var selectedLane: Int = 0) {

    companion object {
        const val TABLE = "cards"
        const val ID = "_id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val FLAVOR_TEXT = "flavor_text"
        const val ICON_URL = "icon_url"
        const val MILL = "mill"
        const val MILL_PREMIUM = "mill_premium"
        const val SCRAP = "scrap"
        const val SCRAP_PREMIUM = "scrap_premium"
        const val POWER = "power"
        const val FACTION = "faction"
        const val LANE = "lane"
        const val LOYALTY = "loyalty"
        const val RARITY = "rarity"
        const val TYPE = "type"
        const val SELECTED_LANE = "selected_lane"

        val MAP1 = Func1<Cursor, Card> { cursor ->
            val card = Card()
            card.cardId = cursor.getIntFromColumn(Card.ID)
            card.name = cursor.getStringFromColumn(Card.NAME)
            card.description = cursor.getStringFromColumn(Card.DESCRIPTION)
            card.flavorText = cursor.getStringFromColumn(Card.FLAVOR_TEXT)
            card.iconUrl = cursor.getStringFromColumn(Card.ICON_URL)
            card.mill = cursor.getIntFromColumn(Card.MILL)
            card.millPremium = cursor.getIntFromColumn(Card.MILL_PREMIUM)
            card.scrap = cursor.getIntFromColumn(Card.SCRAP)
            card.scrapPremium = cursor.getIntFromColumn(Card.SCRAP_PREMIUM)
            card.faction = cursor.getIntFromColumn(Card.FACTION)
            card.lane = cursor.getIntFromColumn(Card.LANE)
            card.rarity = cursor.getIntFromColumn(Card.RARITY)
            card.cardType = cursor.getIntFromColumn(Card.TYPE)
            card.power = cursor.getIntFromColumn(Card.POWER)

            try {
                card.selectedLane = cursor.getIntFromColumn(Card.SELECTED_LANE)
            } catch (e: Exception) {
                card.selectedLane = 0
            }

            card
        }

        val MAPPER = Function<Cursor, Card> { cursor ->
            val card = Card()
            card.cardId = cursor.getIntFromColumn(Card.ID)
            card.name = cursor.getStringFromColumn(Card.NAME)
            card.description = cursor.getStringFromColumn(Card.DESCRIPTION)
            card.flavorText = cursor.getStringFromColumn(Card.FLAVOR_TEXT)
            card.iconUrl = cursor.getStringFromColumn(Card.ICON_URL)
            card.mill = cursor.getIntFromColumn(Card.MILL)
            card.millPremium = cursor.getIntFromColumn(Card.MILL_PREMIUM)
            card.scrap = cursor.getIntFromColumn(Card.SCRAP)
            card.scrapPremium = cursor.getIntFromColumn(Card.SCRAP_PREMIUM)
            card.faction = cursor.getIntFromColumn(Card.FACTION)
            card.lane = cursor.getIntFromColumn(Card.LANE)
            card.rarity = cursor.getIntFromColumn(Card.RARITY)
            card.cardType = cursor.getIntFromColumn(Card.TYPE)
            card.power = cursor.getIntFromColumn(Card.POWER)

            try {
                card.selectedLane = cursor.getIntFromColumn(Card.SELECTED_LANE)
            } catch (e: Exception) {
                card.selectedLane = 0
            }

            card
        }
    }

}
