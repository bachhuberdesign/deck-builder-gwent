package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckbuildController
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Lane
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
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
        @JvmStatic val TAG: String = CardViewerController::class.java.name
    }

    @Inject
    lateinit var presenter: CardViewerPresenter

    @Inject
    lateinit var gson: Gson

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FastItemAdapter<CardItem>

    var isAddCardButtonClickable = true
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

                // Check if clickable to prevent duplicate presenter calls
                if (isAddCardButtonClickable) {
                    isAddCardButtonClickable = false
                    presenter.checkCardAddable(item.card, deckId)
                }
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

    override fun showLaneSelection(lanesToDisplay: List<Int>, card: Card) {
        val laneNames: MutableList<String> = ArrayList()

        lanesToDisplay.forEach { laneInt ->
            laneNames.add(activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(laneInt)))
        }

        MaterialDialog.Builder(activity!!)
                .title("Choose A Lane To Display This Card.")
                .items(laneNames)
                .itemsCallbackSingleChoice(0, { dialog, view, which, text ->
                    when (text) {
                        activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(Lane.EVENT)) -> card.selectedLane = Lane.EVENT
                        activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(Lane.MELEE)) -> card.selectedLane = Lane.MELEE
                        activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(Lane.RANGED)) -> card.selectedLane = Lane.RANGED_SIEGE
                        activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(Lane.SIEGE)) -> card.selectedLane = Lane.SIEGE
                        else -> {
                            throw UnsupportedOperationException("Selected lane does not match Event/Melee/Ranged/Siege. Lane text: $text")
                        }
                    }

                    (parentController as DeckbuildController).addCardToCurrentDeck(card)

                    true
                })
                .positiveText(android.R.string.ok)
                .show()
    }

    override fun onListFiltered(filteredCards: List<Card>) {
        TODO("Method not yet implemented")
    }

    override fun onCardChecked(card: Card, isCardAddable: Boolean) {
        Log.d(TAG, "onCardChecked: Name: ${card.name}, ID: ${card.cardId}, Addable: $isCardAddable")

        if (isCardAddable) {
            (parentController as DeckbuildController).addCardToCurrentDeck(card)
        }

        isAddCardButtonClickable = true
    }

}