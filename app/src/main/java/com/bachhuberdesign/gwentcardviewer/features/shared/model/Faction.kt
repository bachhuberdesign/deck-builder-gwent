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
        var power: String = "",
        var leaders: MutableList<Card> = ArrayList(),
        var iconUrl: String = "") {

    companion object {
        const val NORTHERN_REALMS = 1
        const val SCOIATAEL = 2
        const val MONSTERS = 3
        const val SKELLIGE = 4
        const val NILFGAARD = 5
        const val TABLE = "factions"
        const val ID = "id"
        const val NAME = "name"
        const val POWER = "power"
        const val ICON_URL = "icon_url"

        val MAPPER = Function<Cursor, Faction> { cursor ->
            val faction = Faction()
            faction.id = cursor.getIntFromColumn(Faction.ID)
            faction.name = cursor.getStringFromColumn(Faction.NAME)
            faction.power = cursor.getStringFromColumn(Faction.POWER)
            faction.iconUrl = cursor.getStringFromColumn(Faction.ICON_URL)
            faction
        }
    }


}