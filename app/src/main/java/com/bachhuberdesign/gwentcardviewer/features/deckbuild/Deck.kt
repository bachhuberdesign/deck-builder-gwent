package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class Deck(var id: Int = 0,
                var name: String = "",
                var faction: Faction? = null,
                var cards: MutableList<Card> = ArrayList(),
                var isFavorited: Boolean = false,
                var createdDate: Date = Date(),
                var lastUpdate: Date? = Date()) {

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val FACTION = "faction"
        const val FAVORITED = "favorited"
        const val CREATED_DATE = "created_date"
        const val LAST_UPDATE = "last_update"
    }

}