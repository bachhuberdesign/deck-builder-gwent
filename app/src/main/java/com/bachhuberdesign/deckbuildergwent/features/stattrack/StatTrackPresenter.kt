package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.util.Log
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
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

    override fun attach(view: StatTrackMvpContract) {
        super.attach(view)

        // TODO: Observe here
    }

    override fun detach() {
        super.detach()
    }

    fun addMatch(match: Match) {
        val matchId = statTrackRepository.saveMatch(match)

        Log.d(TAG, "addMatch() matchId: $matchId")
    }

    fun loadStats() {
        val wins = 454
        val losses = 395
        val ties = 16

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

        val stackedEntry = createStackedBarEntryForFaction(Faction.SKELLIGE, 320, 244, 35)
        val stackedEntry2 = createStackedBarEntryForFaction(Faction.MONSTERS, 65, 34, 0)
        val stackedEntry3 = createStackedBarEntryForFaction(Faction.NILFGAARD, 105, 195, 0)
        val stackedEntry4 = createStackedBarEntryForFaction(Faction.NORTHERN_REALMS, 105, 195, 0)
        val stackedEntry5 = createStackedBarEntryForFaction(Faction.SCOIATAEL, 105, 195, 0)

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
        val percents = calculateWinLossTiePercents(wins = wins, losses = losses, ties = ties)
        val entry = BarEntry(faction.toFloat(), floatArrayOf(wins.toFloat(), losses.toFloat(), ties.toFloat()))

        return entry
    }

}