package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.database.Cursor
import com.bachhuberdesign.deckbuildergwent.util.getIntFromColumn
import com.bachhuberdesign.deckbuildergwent.util.getLongFromColumn
import com.bachhuberdesign.deckbuildergwent.util.getStringFromColumn
import io.reactivex.functions.Function
import rx.functions.Func1
import java.util.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class Match(var id: Int = 0,
                 var deckId: Int = 0,
                 var outcome: Int = 0,
                 var opponentFaction: Int = 0,
                 var opponentLeader: Int = 0,
                 var notes: String = "",
                 var playedDate: Date = Date(),
                 var createdDate: Date = Date(),
                 var lastUpdate: Date = Date()) {

    companion object {
        @JvmStatic val TAG: String = Match::class.java.name
        const val TABLE = "matches"
        const val ID = "_id"
        const val DECK_ID = "deck_id"
        const val OUTCOME = "outcome"
        const val OPPONENT_FACTION = "opponent_faction"
        const val OPPONENT_LEADER = "opponent_leader"
        const val NOTES = "notes"
        const val PLAYED_DATE = "played_date"
        const val CREATED_DATE = "created_date"
        const val LAST_UPDATE = "last_update"

        val MAPPER = Function<Cursor, Match> { cursor ->
            val match = Match()

            match.id = cursor.getIntFromColumn(Match.ID)
            match.deckId = cursor.getIntFromColumn(Match.DECK_ID)
            match.outcome = cursor.getIntFromColumn(Match.OUTCOME)
            match.opponentFaction = cursor.getIntFromColumn(Match.OPPONENT_FACTION)
            match.opponentLeader = cursor.getIntFromColumn(Match.OPPONENT_LEADER)
            match.notes = cursor.getStringFromColumn(Match.NOTES)
            match.playedDate = Date(cursor.getLongFromColumn(Match.PLAYED_DATE))
            match.createdDate = Date(cursor.getLongFromColumn(Match.CREATED_DATE))
            match.lastUpdate = Date(cursor.getLongFromColumn(Match.LAST_UPDATE))

            match
        }

        val MAP1 = Func1<Cursor, Match> { cursor ->
            val match = Match()

            match.id = cursor.getIntFromColumn(Match.ID)
            match.deckId = cursor.getIntFromColumn(Match.DECK_ID)
            match.outcome = cursor.getIntFromColumn(Match.OUTCOME)
            match.opponentFaction = cursor.getIntFromColumn(Match.OPPONENT_FACTION)
            match.opponentLeader = cursor.getIntFromColumn(Match.OPPONENT_LEADER)
            match.notes = cursor.getStringFromColumn(Match.NOTES)
            match.playedDate = Date(cursor.getLongFromColumn(Match.PLAYED_DATE))
            match.createdDate = Date(cursor.getLongFromColumn(Match.CREATED_DATE))
            match.lastUpdate = Date(cursor.getLongFromColumn(Match.LAST_UPDATE))

            match
        }

    }

}