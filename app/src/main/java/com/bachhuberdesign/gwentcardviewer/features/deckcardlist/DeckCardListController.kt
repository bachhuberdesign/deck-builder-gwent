package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.deckselect.DeckSelectController
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
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
class DeckCardListController : Controller(), DeckCardListMvpContract, SimpleSwipeCallback.ItemSwipeCallback {

    companion object {
        @JvmStatic val TAG: String = DeckSelectController::class.java.name
    }

    @Inject
    lateinit var presenter: DeckCardListPresenter

    var recyclerView: RecyclerView? = null
    var fastItemAdapter: FastItemAdapter<AbstractItem<*, *>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deck_card_list)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        fastItemAdapter = FastItemAdapter()

        val layoutManager = LinearLayoutManager(activity)
        recyclerView = view.recycler_view
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

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)

        val abstractItems: MutableList<AbstractItem<*, *>> = LinkedList()

        val leaderHeader = HeaderItem()
        leaderHeader.leftText = "Leader"
        abstractItems.add(leaderHeader)

        val cardsHeader = HeaderItem()
        cardsHeader.leftText = "Cards: "
        cardsHeader.rightText = "0"
        abstractItems.add(cardsHeader)

        val meleeSubHeader = SubHeaderItem()
        meleeSubHeader.textLeft = "Melee"
        abstractItems.add(meleeSubHeader)

        val meleeSwipeItem = SlimSwipeableCardItem().withIsSwipeable(true)
        abstractItems.add(meleeSwipeItem)

        val rangedSubHeader = SubHeaderItem()
        rangedSubHeader.textLeft = "Ranged"
        abstractItems.add(rangedSubHeader)

        val siegeSubHeader = SubHeaderItem()
        siegeSubHeader.textLeft = "Siege"
        abstractItems.add(siegeSubHeader)

        val eventSubHeader = SubHeaderItem()
        eventSubHeader.textLeft = "Event"
        abstractItems.add(eventSubHeader)

        fastItemAdapter?.add(abstractItems)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun itemSwiped(var1: Int, var2: Int) {
        Log.d(TAG, "itemSwiped() var1: $var1, var2: $var2")
    }

}