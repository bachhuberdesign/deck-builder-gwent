package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.content.ContentValues
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
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
     * Inserts a new row into [Match.TABLE] *OR* updates the row if an id is already set.
     *
     * @param match Match to persist
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
     * Deletes a row from [Match.TABLE].
     *
     * @param matchId ID of the match to be deleted.
     * @return [Boolean] True if match was successfully deleted, false if no row deleted.
     */
    fun deleteMatch(matchId: Int): Boolean {
        return database.delete(Match.TABLE, "${Match.ID} = $matchId") > 0
    }

    /**
     *
     * @return [Match]
     */
    fun getMostRecentMatch(): Match? {
        val cursor = database.query("SELECT * FROM ${Match.TABLE} " +
                "ORDER BY ${Match.LAST_UPDATE} DESC " +
                "LIMIT 1")

        cursor.use { cursor ->
            if (cursor.moveToNext()) {
                return Match.MAPPER.apply(cursor)
            } else {
                return null
            }
        }
    }

    /**
     * @param deckId ID of the deck to observe.
     */
    fun observeMatchesForDeck(deckId: Int): Observable<MutableList<Match>> {
        val query = "SELECT * FROM ${Match.TABLE} " +
                "WHERE ${Match.DECK_ID} = $deckId"

        return database.createQuery(Match.TABLE, query)
                .mapToList(Match.MAP1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}