package com.bachhuberdesign.deckbuildergwent.features.stattrack

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
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

        val stackedEntry = createStackedBarEntryForFaction("Skellige", 57, 56, 3)
        view!!.showStatsPerFactionsStackedBarChart(arrayListOf(stackedEntry))
    }

    private fun calculateWinLossTiePercents(wins: Int, losses: Int, ties: Int): Triple<Float, Float, Float> {
        return Triple(
                wins / (wins + losses + ties).toFloat(),
                losses / (wins + losses + ties).toFloat(),
                ties / (wins + losses + ties).toFloat())
    }

    private fun createStackedBarEntryForFaction(faction: String, wins: Int, losses: Int, ties: Int): BarEntry {
        val percents = calculateWinLossTiePercents(wins = wins, losses = losses, ties = ties)
        val entry = BarEntry(0f, floatArrayOf(percents.first * 100, percents.second * 100, percents.third * 100))

        return entry
    }

}