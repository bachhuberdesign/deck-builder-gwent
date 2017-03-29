package com.bachhuberdesign.gwentcardviewer.features.shared.model

import android.database.Cursor
import com.bachhuberdesign.gwentcardviewer.util.getIntFromColumn
import com.bachhuberdesign.gwentcardviewer.util.getStringFromColumn
import io.reactivex.functions.Function

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
                var millCost: Pair<Int, Int> = Pair(0, 0),
                var scrapCost: Pair<Int, Int> = Pair(0, 0),
                var power: Int = 0,
                var faction: Int = 0,
                var lane: Int = 0,
                var loyalty: Int = 0,
                var rarity: Int = 0,
                var cardType: Int = 0) {

    companion object {
        const val TABLE = "cards"
        const val ID = "id"
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

        val MAPPER = Function<Cursor, Card> { cursor ->
            val card = Card()
            card.cardId = cursor.getIntFromColumn(Card.ID)
            card.name = cursor.getStringFromColumn(Card.NAME)
            card.description = cursor.getStringFromColumn(Card.DESCRIPTION)
            card.flavorText = cursor.getStringFromColumn(Card.FLAVOR_TEXT)
            card.iconUrl = cursor.getStringFromColumn(Card.ICON_URL)
            card.millCost = Pair(cursor.getIntFromColumn(Card.MILL), cursor.getIntFromColumn(Card.MILL_PREMIUM))
            card.scrapCost = Pair(cursor.getIntFromColumn(Card.SCRAP), cursor.getIntFromColumn(Card.SCRAP_PREMIUM))
            card.faction = cursor.getIntFromColumn(Card.FACTION)
            card.lane = cursor.getIntFromColumn(Card.LANE)
            card.rarity = cursor.getIntFromColumn(Card.RARITY)
            card.cardType = cursor.getIntFromColumn(Card.TYPE)
            card
        }
    }

}
