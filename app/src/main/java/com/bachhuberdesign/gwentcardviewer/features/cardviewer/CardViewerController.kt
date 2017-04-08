package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.google.gson.Gson
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.controller_cardviewer.view.*
import javax.inject.Inject

// TODO: Add RecyclerView and FastAdapterItme


/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class CardViewerController : Controller, CardViewerMvpContract {

    constructor(filters: CardFilters, isDeckbuildMode: Boolean) : super() {
        this.filters = filters
        this.isDeckbuildMode = isDeckbuildMode
    }

    constructor(args: Bundle) : super()

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    @Inject
    lateinit var presenter: CardViewerPresenter

    @Inject
    lateinit var gson: Gson

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FastItemAdapter<CardItem>

    var filters: CardFilters? = null
    var isDeckbuildMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_cardviewer)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (filters == null) {
            filters = gson.fromJson(args.getString("filters"), CardFilters::class.java)
        }

        if (!isDeckbuildMode) {
            isDeckbuildMode = args.getBoolean("isDeckbuildMode", false)
        }

        adapter = FastItemAdapter()
        adapter.withItemEvent(object : ClickEventHook<CardItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return (viewHolder as CardItem.ViewHolder).addCardButton
            }

            override fun onClick(v: View, position: Int, adapter: FastAdapter<CardItem>, item: CardItem) {
                // TODO: Add presenter call here
                Toast.makeText(activity, "Add card button clicked", Toast.LENGTH_LONG).show()
            }
        })

        val layoutManager = LinearLayoutManager(activity)

        recyclerView = view.recycler_view
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.getCardsFiltered(filters!!)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("filters", gson.toJson(filters))
        outState.putBoolean("isDeckbuildMode", isDeckbuildMode)
        super.onSaveInstanceState(outState)
    }

    private fun refreshFilters(filters: CardFilters) {
        presenter.getCardsFiltered(filters)
    }

    override fun onCardsLoaded(cards: List<Card>) {
        cards.forEach { card ->
            adapter.add(CardItem(card, isDeckbuildMode))
        }

        adapter.notifyDataSetChanged()
    }

    override fun onListFiltered(filteredCards: List<Card>) {
        TODO("Method not yet implemented")
    }

}