package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.content.ContentValues
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import com.squareup.sqlbrite.BriteDatabase
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class StatTrackRepository @Inject constructor(val database: BriteDatabase) {

    companion object {
        @JvmStatic val TAG: String = StatTrackRepository::class.java.name
    }

    /**
     *
     */
    fun saveMatch(match: Match): Int {
        val values = ContentValues()

        values.put(Match.DECK_ID, match.deckId)
        values.put(Match.OUTCOME, match.outcome)
        values.put(Match.OPPONENT_FACTION, match.opponentFaction)
        values.put(Match.OPPONENT_LEADER, match.opponentLeader)
        values.put(Match.NOTES, match.notes)
        values.put(Match.PLAYED_DATE, match.playedDate.time)
        values.put(Match.CREATED_DATE, match.createdDate.time)
        values.put(Match.LAST_UPDATE, match.lastUpdate.time)

        if (match.id == 0) {
            match.id = database.insert(Match.TABLE, values).toInt()
        } else {
            database.update(Match.TABLE, values, "${Match.ID} = ${match.id}")
        }

        return match.id
    }

    /**
     *
     */
    fun deleteMatch(matchId: Int) {
        // TODO:
    }

}