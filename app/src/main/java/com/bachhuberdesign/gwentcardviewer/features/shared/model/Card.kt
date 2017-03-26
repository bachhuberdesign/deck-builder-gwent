package com.bachhuberdesign.gwentcardviewer.features.shared.model

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class Card(val cardId: Int = 0,
                val name: String = "",
                val cardText: String = "",
                val faction: Faction?,
                val flavorText: String = "",
                val iconUrl: String = "",
                val lane: Lane?,
                val millCost: Pair<Int, Int> = Pair(0, 0),
                val scrapCost: Pair<Int, Int> = Pair(0, 0),
                val rarity: Rarity?,
                val cardType: CardType?)
