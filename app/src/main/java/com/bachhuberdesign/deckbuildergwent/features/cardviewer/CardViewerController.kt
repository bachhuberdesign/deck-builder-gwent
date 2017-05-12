package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckbuildController
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Lane
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.SharedElementDelayingChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.getStringResourceByName
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandlerCompat
import com.google.gson.Gson
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.helpers.ClickListenerHelper
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

    var currentSortMethod: Int = 0
    var isSortAscending = true
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

        if (deckId > 0) {
            (activity as MainActivity).displayHomeAsUp(true)
        } else {
            activity?.title = "Card Database"
        }

        setHasOptionsMenu(true)
        initRecyclerView(view)

        return view
    }

    override fun onAttach(view: View) {
        Log.d(TAG, "onAttach()")
        super.onAttach(view)
        presenter.attach(this)

        // TODO: Take getCards() call off UI thread
        presenter.getCards(filters!!, deckId)
    }

    override fun onDetach(view: View) {
        Log.d(TAG, "onDetach()")
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
                .icon(FontAwesome.Icon.faw_sort_amount_desc)
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
            R.id.menu_filter_cards -> showSortingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortingDialog() {
        val items = arrayListOf(activity!!.getString(R.string.sort_name), activity!!.getString(R.string.sort_type),
                activity!!.getString(R.string.sort_scrap_cost), activity!!.getString(R.string.sort_faction),
                activity!!.getString(R.string.sort_lane))

        MaterialDialog.Builder(activity!!)
                .title(R.string.sort_title)
                .items(items)
                .itemsCallbackSingleChoice(currentSortMethod, { dialog, view, which, text ->
                    isSortAscending = dialog.isPromptCheckBoxChecked
                    currentSortMethod = which

                    when (which) {
                        0 -> adapter.itemAdapter.withComparator(CardItem.CardNameComparator(isSortAscending))
                        1 -> adapter.itemAdapter.withComparator(CardItem.CardTypeComparator(isSortAscending))
                        2 -> adapter.itemAdapter.withComparator(CardItem.CardScrapCostComparator(isSortAscending))
                        3 -> adapter.itemAdapter.withComparator(CardItem.CardFactionComparator(isSortAscending))
                        4 -> adapter.itemAdapter.withComparator(CardItem.CardLaneComparator(isSortAscending))
                    }

                    true
                })
                .positiveText(R.string.confirm)
                .negativeText(android.R.string.cancel)
                .checkBoxPromptRes(R.string.checkbox_sort_ascending, isSortAscending, null)
                .show()
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
        adapter.withOnClickListener({ view, adapter, item, position ->
            val imageTransitionName = "imageTransition${item.card.cardId}"
            val nameTransitionName = "nameTransition${item.card.cardId}"

            val transitionNames = java.util.ArrayList<String>()
            transitionNames.add(imageTransitionName)
            transitionNames.add(nameTransitionName)

            router.pushController(RouterTransaction.with(CardDetailController(item.card.cardId))
                    .tag(DeckbuildController.TAG)
                    .pushChangeHandler(TransitionChangeHandlerCompat(SharedElementDelayingChangeHandler(transitionNames), FadeChangeHandler()))
                    .popChangeHandler(TransitionChangeHandlerCompat(SharedElementDelayingChangeHandler(transitionNames), FadeChangeHandler())))
            true
        })

        adapter.withItemEvent(object : ClickEventHook<CardItem>() {
            override fun onBindMany(viewHolder: RecyclerView.ViewHolder): MutableList<View>? {
                if (viewHolder is CardItem.ViewHolder) {
                    return ClickListenerHelper.toList(viewHolder.removeCardButton, viewHolder.addCardButton)
                } else {
                    return super.onBindMany(viewHolder)
                }
            }

            override fun onClick(v: View, position: Int, adapter: FastAdapter<CardItem>, item: CardItem) {
                if (v.tag == "add") {
                    // Check if clickable to prevent duplicate presenter calls
                    if (isAddCardButtonClickable) {
                        isAddCardButtonClickable = false
                        presenter.checkCardAddable(item.card, deckId)
                    }
                } else if (v.tag == "remove") {
                    if (item.count > 0) {
                        presenter.removeCardFromDeck(deckId, item.card)
                    }
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
        presenter.getCards(filters, deckId)
    }

    override fun onDeckbuildModeCardsLoaded(cards: List<Card>, deck: Deck) {
        cards.forEach { card ->
            val cardItem = CardItem(card, true)
            cardItem.count = deck.cards.filter { it.cardId == card.cardId }.size
            adapter.add(cardItem)
        }
    }

    override fun handleBack(): Boolean {
        return super.handleBack()
    }

    override fun onCardChecked(card: Card, isCardAddable: Boolean) {
        if (isCardAddable) {
            presenter.addCardToDeck(deckId, card)
        }

        isAddCardButtonClickable = true
    }

    override fun updateCount(card: Card, itemRemoved: Boolean) {
        Log.d(TAG, "updateCount()")
        val item = adapter.adapterItems.find { it.card.cardId == card.cardId }

        val position = adapter.adapterItems.indexOf(item)

        if (itemRemoved) {
            adapter.adapterItems.find { it.card.cardId == card.cardId }!!.count -= 1
        } else {
            adapter.adapterItems.find { it.card.cardId == card.cardId }!!.count += 1
        }

        adapter.notifyAdapterItemChanged(position)
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
                    presenter.addCardToDeck(deckId, card)

                    isAddCardButtonClickable = true

                    true
                })
                .cancelListener { dialog -> isAddCardButtonClickable = true }
                .positiveText(android.R.string.ok)
                .show()
    }

    override fun onViewModeCardsLoaded(cards: List<Card>) {
        cards.forEach { card ->
            val cardItem = CardItem(card, false)
            adapter.add(cardItem)
        }
    }

    override fun onListFiltered(filteredCards: List<Card>) {
        Log.d(TAG, "onListFiltered()")
    }

}
