package com.bachhuberdesign.deckbuildergwent.features.stattrack

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface StatTrackMvpContract : MvpContract {

    fun onDeckLoaded(deck: Deck)

    fun onNoDeckAvailable()

    fun showOverallWinPieChart(entries: List<PieEntry>)

    fun showWinsTrendLineChart(entries: List<Entry>)

    fun showStatsPerFactionsStackedBarChart(entries: List<BarEntry>)

    fun onMatchAdded()

}