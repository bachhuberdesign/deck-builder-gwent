package com.bachhuberdesign.gwentcardviewer.features.shared.model

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class Card(val cardId: Int = 0,
                val name: String = "",
                val description: String = "",
                val flavorText: String = "",
                val iconUrl: String = "",
                val millCost: Pair<Int, Int> = Pair(0, 0),
                val scrapCost: Pair<Int, Int> = Pair(0, 0),
                val faction: Faction? = null,
                val lane: Lane? = null,
                val rarity: Rarity? = null,
                val cardType: CardType? = null) {

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val FLAVOR_TEXT = "flavor_text"
        const val ICON_URL = "icon_url"
        const val MILL = "mill"
        const val MILL_PREMIUM = "mill_premium"
        const val SCRAP = "scrap"
        const val SCRAP_PREMIUM = "scrap_premium"
        const val FACTION = "faction"
        const val LANE = "lane"
        const val RARITY = "rarity"
        const val TYPE = "type"
    }

}
