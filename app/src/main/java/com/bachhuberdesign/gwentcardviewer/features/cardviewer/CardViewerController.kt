package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckbuildController
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

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class CardViewerController : Controller, CardViewerMvpContract {

    constructor(filters: CardFilters) : super() {
        this.filters = filters
    }

    constructor(filters: CardFilters, deckId: Int) {
        this.filters = filters
        this.deckId = deckId
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
    var deckId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_cardviewer)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (filters == null) {
            filters = gson.fromJson(args.getString("filters"), CardFilters::class.java)
        }

        if (deckId == 0) {
            deckId = args.getInt("deckId", 0)
        }

        adapter = FastItemAdapter()
        adapter.withItemEvent(object : ClickEventHook<CardItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return (viewHolder as CardItem.ViewHolder).addCardButton
            }

            override fun onClick(v: View, position: Int, adapter: FastAdapter<CardItem>, item: CardItem) {
                presenter.checkCardAddable(item.card, deckId)
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
        outState.putInt("deckId", deckId)
        super.onSaveInstanceState(outState)
    }

    private fun refreshFilters(filters: CardFilters) {
        presenter.getCardsFiltered(filters)
    }

    override fun onCardsLoaded(cards: List<Card>) {
        cards.forEach { card ->
            adapter.add(CardItem(card, isDeckbuildMode = deckId > 0))
        }

        adapter.notifyDataSetChanged()
    }

    override fun onListFiltered(filteredCards: List<Card>) {
        TODO("Method not yet implemented")
    }

    override fun onCardChecked(card: Card) {
        Log.d(TAG, "onCardChecked: Name: ${card.name}, ID: ${card.cardId}")

        val deckbuildController = router.getControllerWithTag(DeckbuildController.TAG) as DeckbuildController
        deckbuildController.addCardToCurrentDeck(card.cardId)

        router.handleBack()
        // TODO: Animate
    }

}