package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.Deck
import com.bachhuberdesign.gwentcardviewer.features.deckselect.DeckSelectController
import com.bachhuberdesign.gwentcardviewer.features.shared.model.CardType
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Lane
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeCallback
import kotlinx.android.synthetic.main.controller_deck_select.view.*
import java.util.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckCardListController : Controller, DeckCardListMvpContract, SimpleSwipeCallback.ItemSwipeCallback {

    constructor(deckId: Int) {
        this.deckId = deckId
    }

    constructor(args: Bundle) : super()

    companion object {
        @JvmStatic val TAG: String = DeckSelectController::class.java.name
    }

    @Inject
    lateinit var presenter: DeckCardListPresenter

    var recyclerView: RecyclerView? = null
    var fastItemAdapter: FastItemAdapter<AbstractItem<*, *>>? = null
    var deckId: Int = 0
    val items: MutableList<AbstractItem<*, *>> = LinkedList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deck_card_list)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (deckId == 0) {
            deckId = args.getInt("deckId", 0)
        }

        initRecyclerView(view)

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadDeck(deckId)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("deckId", deckId)
        super.onSaveInstanceState(outState)
    }

    private fun initRecyclerView(v: View) {
        fastItemAdapter = FastItemAdapter()

        val layoutManager = LinearLayoutManager(activity)
        recyclerView = v.recycler_view
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = fastItemAdapter

        val touchCallback = ImprovedSwipeCallback(this,
                activity!!.getDrawable(R.drawable.leak_canary_icon),
                ItemTouchHelper.LEFT,
                ContextCompat.getColor(activity, R.color.md_red_900))
                .withBackgroundSwipeRight(ContextCompat.getColor(activity, R.color.md_blue_900))
                .withLeaveBehindSwipeRight(activity!!.getDrawable(R.drawable.leak_canary_icon))

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    private fun buildItemList(deck: Deck) {
        if (items.size > 0) {
            items.clear()
        }
        addHeaderItems(deck)
        addLaneItems(Lane.MELEE, deck)
        addLaneItems(Lane.RANGED, deck)
        addLaneItems(Lane.SIEGE, deck)
        addLaneItems(Lane.EVENT, deck)
        addFooterItems(deck)
    }

    private fun addHeaderItems(deck: Deck?) {
        val leaderHeader = HeaderItem()
        leaderHeader.leftText = "Leader"
        items.add(leaderHeader)

        val leaderItem = SlimCardItem().withIsSwipeable(false)
        if (deck?.cards?.size!! > 0) {
            leaderItem.card = deck.cards[0]
        }
        items.add(leaderItem)

        val cardsHeader = HeaderItem()
        cardsHeader.leftText = "Cards: "
        cardsHeader.rightText = "0"
        items.add(cardsHeader)
    }

    private fun addLaneItems(lane: Int, deck: Deck) {
        val laneText: String = activity!!.getStringResourceByName(Lane.ID_TO_KEY.apply(lane))

        val laneSubHeader = SubHeaderItem()
        laneSubHeader.leftText = laneText
        laneSubHeader.rightText = "0"
        laneSubHeader.withTag("lane_${lane}_header")
        items.add(laneSubHeader)

        deck.cards.filter { it.selectedLane == lane }
                .filterNot { it.cardType == CardType.LEADER }
                .forEach { card ->
                    val cardItem = SlimCardItem().withIsSwipeable(true)
                    cardItem.card = card
                    items.add(cardItem)
                }
    }

    private fun addFooterItems(deck: Deck) {
    }

    override fun itemSwiped(position: Int, var2: Int) {
        val item = fastItemAdapter?.getItem(position)
        if (item?.getType() == SlimCardItem.TYPE) {
            presenter.removeCardFromDeck((item as SlimCardItem).card, deckId)
        }

        fastItemAdapter?.remove(position)
    }

    override fun onDeckLoaded(deck: Deck) {
        Log.d(TAG, "Deck loaded $deck")

        if (fastItemAdapter != null && fastItemAdapter!!.adapterItems.size > 0) {
            fastItemAdapter?.clear()
        }

        buildItemList(deck)
        fastItemAdapter?.add(items)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}