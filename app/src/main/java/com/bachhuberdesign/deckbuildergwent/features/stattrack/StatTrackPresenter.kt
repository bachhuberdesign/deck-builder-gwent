package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.util.Log
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Outcome
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import rx.Subscription
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class StatTrackPresenter
@Inject constructor(private val deckRepository: DeckRepository,
                    private val statTrackRepository: StatTrackRepository) : BasePresenter<StatTrackMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = StatTrackPresenter::class.java.name
    }

    private var subscription: Subscription? = null

    override fun detach() {
        if (subscription != null) {
            subscription!!.unsubscribe()
        }

        super.detach()
    }

    /**
     *
     */
    fun loadRecentDeck() {
        val match = statTrackRepository.getMostRecentMatch()
        val deckToLoad: Deck?

        if (match != null && match.deckId > 0) {
            deckToLoad = deckRepository.getDeckById(match.deckId)
        } else {
            deckToLoad = deckRepository.getMostRecentDeck()
        }

        if (deckToLoad != null) {
            getViewOrThrow().onDeckLoaded(deckToLoad)
        } else {
            getViewOrThrow().onNoDeckAvailable()
        }
    }

    /**
     *
     */
    fun observeStats(deckId: Int) {
        subscription = statTrackRepository.observeMatchesForDeck(deckId)
                .subscribe({ matches ->
                    loadStats(matches)
                }, { error ->
                    Log.e(TAG, "Error querying matches for deckId $deckId.", error)
                })
    }

    /**
     *
     */
    fun addMatch(match: Match) {
        if (match.deckId <= 0) {
            throw MatchException("A valid deck ID must be set.")
        }

        val matchId = statTrackRepository.saveMatch(match)

        getViewOrThrow().onMatchAdded()
    }

    /**
     *
     */
    fun deleteMatch(match: Match) {
        statTrackRepository.deleteMatch(match.id)
    }

    /**
     *
     */
    fun loadStats(matches: List<Match>) {
        val wins = matches.filter { it.outcome == Outcome.WIN }.count()
        val losses = matches.filter { it.outcome == Outcome.LOSS }.count()
        val ties = matches.filter { it.outcome == Outcome.TIE }.count()

        val totalPercentages = calculateWinLossTiePercents(wins = wins, losses = losses, ties = ties)
        val entries: MutableList<PieEntry> = ArrayList()

        if (totalPercentages.first > 0f) {
            entries.add(PieEntry(totalPercentages.first, "Win"))
        }

        if (totalPercentages.second > 0f) {
            entries.add(PieEntry(totalPercentages.second, "Loss"))
        }

        if (totalPercentages.third > 0f) {
            entries.add(PieEntry(totalPercentages.third, "Tie"))
        }

        view!!.showOverallWinPieChart(entries)

        val stackedEntry = createStackedBarEntryForFaction(Faction.NORTHERN_REALMS, 100, 0, 0)
        val stackedEntry2 = createStackedBarEntryForFaction(Faction.SCOIATAEL, 65, 34, 0)
        val stackedEntry3 = createStackedBarEntryForFaction(Faction.MONSTERS, 185, 195, 0)
        val stackedEntry4 = createStackedBarEntryForFaction(Faction.SKELLIGE, 105, 195, 0)
        val stackedEntry5 = createStackedBarEntryForFaction(Faction.NILFGAARD, 1035, 195, 0)

        view!!.showStatsPerFactionsStackedBarChart(arrayListOf(stackedEntry, stackedEntry2, stackedEntry3,
                stackedEntry4, stackedEntry5))
    }

    private fun calculateWinLossTiePercents(wins: Int, losses: Int, ties: Int): Triple<Float, Float, Float> {
        return Triple(
                wins / (wins + losses + ties).toFloat(),
                losses / (wins + losses + ties).toFloat(),
                ties / (wins + losses + ties).toFloat())
    }

    private fun createStackedBarEntryForFaction(faction: Int, wins: Int, losses: Int, ties: Int): BarEntry {
        val percents = calculateWinLossTiePercents(wins = wins, losses = losses, ties = 0)
        val entry = BarEntry(faction.toFloat(), floatArrayOf(percents.first * 100))

        return entry
    }

}