package com.bachhuberdesign.deckbuildergwent.features.stattrack

import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.github.mikephil.charting.data.PieEntry

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface StatTrackMvpContract : MvpContract {

    fun showWinLossChart(entries: List<PieEntry>)

}