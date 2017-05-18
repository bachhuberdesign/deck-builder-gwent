package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.stattrack.addmatchdialog.AddMatchDialogController
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.Constants
import com.bachhuberdesign.deckbuildergwent.util.changehandler.FabToDialogTransitionChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
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
        presenter.loadRecentDeck()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    fun addMatch(match: Match) {
        presenter.addMatch(match)
    }

    override fun onDeckLoaded(deck: Deck) {
        presenter.observeStats(deck.id)
    }

    override fun showWinsTrendLineChart(entries: List<Entry>) {
        val trendChart = view!!.win_percents_trend_chart

        trendChart.description.isEnabled = false
        trendChart.setTouchEnabled(false)

        val set1 = LineDataSet(entries, "DataSet 1")
        set1.axisDependency = AxisDependency.LEFT
        set1.color = ColorTemplate.getHoloBlue()
        set1.valueTextColor = ColorTemplate.getHoloBlue()
        set1.lineWidth = 1.5f
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.fillAlpha = 65
        set1.fillColor = ColorTemplate.getHoloBlue()
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.setDrawCircleHole(false)

        val data = LineData(set1)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)

        trendChart.data = data
        trendChart.invalidate()
    }

    override fun showStatsPerFactionsStackedBarChart(entries: List<BarEntry>) {
        val barChart = view!!.wins_per_faction_bar_chart

        barChart.setDrawGridBackground(false)
        barChart.description.isEnabled = true
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawGridBackground(false)
        barChart.setDrawValueAboveBar(false)
        barChart.description.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawLabels(false)
        xAxis.setDrawGridLines(true)
        xAxis.granularity = 10f

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.setDrawGridLines(true)
        yAxisLeft.setDrawLabels(true)
        yAxisLeft.axisMinimum = 0f

        val yAxisRight = barChart.axisRight
        yAxisRight.setDrawAxisLine(true)
        yAxisRight.setDrawLabels(false)
        yAxisRight.setDrawGridLines(false)
        yAxisRight.axisMinimum = 0f

        val dataSets = ArrayList<IBarDataSet>()
        val dataSetNames = arrayOf("Northern Realms", "Scoia'tael", "Monsters", "Skellige", "Nilfgaard")

        entries.forEachIndexed { i, entry ->
            val dataSet = BarDataSet(listOf(entry), dataSetNames[i])
            dataSet.valueFormatter = PercentFormatter()
            dataSet.setDrawIcons(false)
            dataSet.color = Constants.FLAT_UI_COLORS.toList()[i]

            dataSets.add(dataSet)
        }

        val barData = BarData(dataSets)
        barData.setValueTextSize(10f)
        barData.barWidth = 0.65f
        barData.setValueTextColor(Color.WHITE)
        barData.setValueTextSize(13f)
        barChart.data = barData

        val legend = barChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.formSize = 8f
        legend.isWordWrapEnabled = false
        legend.xEntrySpace = 4f

        barChart.setFitBars(true)
        barChart.animateY(1400)
    }

    override fun showOverallWinPieChart(entries: List<PieEntry>) {
        val pieChart = view!!.win_loss_pie_chart
        pieChart.setUsePercentValues(true)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 37.5f
        pieChart.setHoleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.setDrawCenterText(true)
        pieChart.setCenterTextSize(18f)
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false

        val pieDataSet = PieDataSet(entries, "Match Results")
        pieDataSet.colors = Constants.FLAT_UI_COLORS.toList()

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(13f)
        pieData.setValueTextColor(Color.DKGRAY)
        pieChart.data = pieData

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
    }

    override fun onMatchAdded() {
        router.popController(router.getControllerWithTag(AddMatchDialogController.TAG)!!)
        Toast.makeText(activity!!, "Match added successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onNoDeckAvailable() {
        Toast.makeText(activity!!, "No decks available for stat tracking. Go make one!", Toast.LENGTH_LONG).show()
    }

}