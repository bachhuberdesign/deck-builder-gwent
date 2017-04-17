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
data class Faction(
        var id: Int = 0,
        var name: String = "",
        var effect: String = "",
        var iconUrl: String = "",
        var leaders: MutableList<Card> = ArrayList()
) {

    companion object {
        const val NEUTRAL = 0
        const val NORTHERN_REALMS = 1
        const val SCOIATAEL = 2
        const val MONSTERS = 3
        const val SKELLIGE = 4
        const val NILFGAARD = 5
        const val TABLE = "factions"
        const val ID = "_id"
        const val NAME = "name"
        const val EFFECT = "effect"
        const val ICON_URL = "icon_url"

        val MAPPER = Function<Cursor, Faction> { cursor ->
            val faction = Faction()
            faction.id = cursor.getIntFromColumn(Faction.ID)
            faction.name = cursor.getStringFromColumn(Faction.NAME)
            faction.effect = cursor.getStringFromColumn(Faction.EFFECT)
            faction.iconUrl = cursor.getStringFromColumn(Faction.ICON_URL)
            faction
        }

        val ID_TO_KEY = Function<Int, String> { factionId ->
            when (factionId) {
                NEUTRAL -> "neutral"
                NORTHERN_REALMS -> "northern_realms"
                SCOIATAEL -> "scoiatael"
                MONSTERS -> "monsters"
                SKELLIGE -> "skellige"
                NILFGAARD -> "nilfgaard"
                else -> throw IndexOutOfBoundsException("factionId value was $factionId, expected 0-5.")
            }
        }
    }

}