package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
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
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
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

        setHasOptionsMenu(true)

        if (filters == null) {
            filters = gson.fromJson(args.getString("filters"), CardFilters::class.java)
        }

        if (deckId == 0) {
            deckId = args.getInt("deckId", 0)
        }

        initRecyclerView(view)

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.getCardsFiltered(filters!!, deckId)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_card_viewer, menu)

        menu.findItem(R.id.menu_filter_cards).icon = IconicsDrawable(activity!!)
                .icon(FontAwesome.Icon.faw_filter)
                .color(Color.WHITE)
                .sizeDp(18)

        menu.findItem(R.id.menu_search_cards).icon = IconicsDrawable(activity!!)
                .icon(FontAwesome.Icon.faw_search)
                .color(Color.WHITE)
                .sizeDp(18)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_cards -> showSearchActionView(item)
            R.id.menu_filter_cards -> Log.d(TAG, "menu_filter_cards clicked") // TODO
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSearchActionView(searchItem: MenuItem) {
        val searchView = searchItem.actionView as SearchView
        MenuItemCompat.expandActionView(searchItem)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(queryText: String): Boolean {
                Log.d(TAG, "onQueryTextChange(): $queryText")
                adapter.filter(queryText)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                // Adapter filtering already handled by onQueryTextChange() -- ignore
                return true
            }
        })
    }

    private fun initRecyclerView(v: View) {
        adapter = FastItemAdapter()

        adapter.withItemEvent(object : ClickEventHook<CardItem>() {
            override fun onBind(holder: RecyclerView.ViewHolder): View? {
                return (holder as CardItem.ViewHolder).addCardButton
            }

            override fun onClick(v: View, position: Int, adapter: FastAdapter<CardItem>, item: CardItem) {
                // Check if clickable to prevent duplicate presenter calls
                if (isAddCardButtonClickable) {
                    isAddCardButtonClickable = false
                    presenter.checkCardAddable(item.card, deckId)
                }
            }
        })

        adapter.withFilterPredicate({ item, constraint ->
            val cardNameCondensed: String = item.card.name.replace("[\\W]".toRegex(), "")
            val constraintCondensed: String = constraint.replace("[\\W]".toRegex(), "")
            val descriptionCondensed: String = item.card.description.replace("[\\W]".toRegex(), "")

            // Filter out any item that doesn't match card name or description constraints
            !(cardNameCondensed.contains(constraintCondensed, ignoreCase = true)
                    || descriptionCondensed.contains(constraintCondensed, ignoreCase = true))
        })

        val layoutManager = LinearLayoutManager(activity)

        recyclerView = v.recycler_view
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun refreshFilters(filters: CardFilters) {
        presenter.getCardsFiltered(filters, deckId)
    }

    override fun onDeckbuildModeCardsLoaded(cards: List<Card>, deck: Deck) {
        cards.forEach { card ->
            val cardItem = CardItem(card, true)
            cardItem.count = deck.cards.filter { it.cardId == card.cardId }.size
            adapter.add(cardItem)
        }
    }

    override fun handleBack(): Boolean {
        if (deckId > 0) {
            (parentController as DeckbuildController).closeCardViewerAndAnimate()
            return true
        } else {
            return super.handleBack()
        }
    }

    override fun onViewModeCardsLoaded(cards: List<Card>) {
        TODO("Not yet implemented.")
    }

    override fun onListFiltered(filteredCards: List<Card>) {
        TODO("Method not yet implemented")
    }

    override fun onCardChecked(card: Card, isCardAddable: Boolean) {
        if (isCardAddable) {
            // TODO: Extract method to re-use
            (parentController as DeckbuildController).addCardToCurrentDeck(card)
            val item = adapter.adapterItems.find { it.card.cardId == card.cardId }

            val position = adapter.adapterItems.indexOf(item)
            adapter.adapterItems.find { it.card.cardId == card.cardId }!!.count += 1

            adapter.notifyAdapterItemChanged(position)
        }

        isAddCardButtonClickable = true
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
                        activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(Lane.RANGED)) -> card.selectedLane = Lane.RANGED
                        activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(Lane.SIEGE)) -> card.selectedLane = Lane.SIEGE
                        else -> {
                            throw UnsupportedOperationException("Selected lane does not match Event/Melee/Ranged/Siege. Lane text: $text")
                        }
                    }
                    // TODO: Extract method to re-use
                    (parentController as DeckbuildController).addCardToCurrentDeck(card)

                    val item = adapter.adapterItems.find { it.card.cardId == card.cardId }
                    val position = adapter.adapterItems.indexOf(item)
                    adapter.adapterItems.find { it.card.cardId == card.cardId }!!.count += 1
                    adapter.notifyAdapterItemChanged(position)

                    isAddCardButtonClickable = true

                    true
                })
                .cancelListener { dialog -> isAddCardButtonClickable = true }
                .positiveText(android.R.string.ok)
                .show()
    }

}
