package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.changehandler.FabToDialogTransitionChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.controller_stat_track.view.*
import javax.inject.Inject


/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class StatTrackController : Controller(), StatTrackMvpContract {

    companion object {
        @JvmStatic val TAG: String = StatTrackController::class.java.name
    }

    @Inject
    lateinit var presenter: StatTrackPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_stat_track)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        activity!!.title = "Deck Stats"

        view.fab.setOnClickListener {
            router.pushController(RouterTransaction.with(AddMatchDialogController())
                    .tag(AddMatchDialogController.TAG)
                    .pushChangeHandler(FabToDialogTransitionChangeHandler())
                    .popChangeHandler(FabToDialogTransitionChangeHandler()))
        }

        return view
    }


    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadStats()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    /**
     *
     */
    fun addMatch(match: Match) {
        // TODO: Change to listener?
        presenter.addMatch(match)
    }

    override fun onDeckLoaded(deck: Deck) {
        // TODO:
    }

    override fun showWinsTrendLineChart(entries: List<Entry>) {
        // TODO:
    }

    override fun showStatsPerFactionsStackedBarChart(entries: List<BarEntry>) {

        // TODO: Switch to negative bar chart (stacked?)

        val barChart = view!!.wins_per_faction_bar_chart

        barChart.description.isEnabled = true
        barChart.setPinchZoom(false)

        val sets: MutableList<IBarDataSet> = ArrayList()

        entries.forEach { entry ->
            val barDataSet = BarDataSet(listOf(entry), "Skellige")
            barDataSet.stackLabels = arrayOf("Win %", "Loss %", "Tie %")
            barDataSet.colors = ColorTemplate.LIBERTY_COLORS.toList()
            sets.add(barDataSet)
        }

        val barData = BarData(sets)
        barData.setValueTextSize(12f)

        barChart.data = barData
    }

    override fun showOverallWinPieChart(entries: List<PieEntry>) {
        val pieChart = view!!.win_loss_pie_chart
        pieChart.setUsePercentValues(true)

        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 45f
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(0)

        pieChart.setDrawCenterText(true)
        pieChart.centerText = "All"
        pieChart.setCenterTextSize(18f)

        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        val pieDataSet = PieDataSet(entries, "Match Results")
        pieDataSet.colors = ColorTemplate.LIBERTY_COLORS.toList()

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(13f)
        pieData.setValueTextColor(Color.DKGRAY)
        pieChart.data = pieData

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
    }

}