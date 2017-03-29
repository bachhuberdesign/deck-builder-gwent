package com.bachhuberdesign.gwentcardviewer.features.shared.model

import android.database.Cursor
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
        const val FACTION_NORTHERN_REALMS = 1
        const val FACTION_SCOIATAEL = 2
        const val FACTION_MONSTERS = 3
        const val FACTION_SKELLIGE = 4
        const val FACTION_NILFGAARD = 5
        const val TABLE = "factions"
        const val ID = "id"
        const val NAME = "name"
        const val POWER = "power"
        const val ICON_URL = "icon_url"

        val MAPPER = Function<Cursor, Faction> { cursor ->
            val faction = Faction()
            faction.id = cursor.getInt(cursor.getColumnIndex(Faction.ID))
            faction.name = cursor.getString(cursor.getColumnIndex(Faction.NAME))
            faction.power = cursor.getString(cursor.getColumnIndex(Faction.POWER))
            faction.iconUrl = cursor.getString(cursor.getColumnIndex(Faction.ICON_URL))
            faction
        }
    }


}