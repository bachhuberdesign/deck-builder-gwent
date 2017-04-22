package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import kotlinx.android.synthetic.main.controller_deck_select.view.*
import java.util.*
import javax.inject.Inject


/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckCardListController : Controller(), DeckCardListMvpContract {

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

}