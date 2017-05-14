package com.bachhuberdesign.deckbuildergwent.features.stattrack

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
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
        val winPercentage = 44.3f

        val wins = PieEntry(winPercentage, "Wins")
        val loss = PieEntry(100 - winPercentage, "Losses")

        view!!.showWinLossChart(arrayListOf(wins, loss))
    }

}