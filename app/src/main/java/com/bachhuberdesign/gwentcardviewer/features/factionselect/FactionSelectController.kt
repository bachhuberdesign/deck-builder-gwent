package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckbuildActivity
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.FlipChangeHandler
import com.bachhuberdesign.gwentcardviewer.util.FlipChangeHandler.FlipDirection
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.controller_faction_select.view.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class FactionSelectController : Controller(), FactionSelectMvpContract {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    var recyclerView: RecyclerView? = null
    var adapter: FastItemAdapter<FactionSelectItem>? = null
    var layoutManager: LinearLayoutManager? = null

    @Inject
    lateinit var presenter: FactionSelectPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_faction_select)

        val persistedComponent = (activity as DeckbuildActivity)
                .persistedComponent.activitySubcomponent(ActivityModule(activity as DeckbuildActivity))
        persistedComponent.inject(this)

        adapter = FastItemAdapter()
        adapter?.withOnClickListener { view, adapter, item, position ->
            Toast.makeText(activity, "Item clicked at position $position", Toast.LENGTH_SHORT).show()

            router.pushController(RouterTransaction.with(LeaderConfirmController(item.leaders!![0]))
                    .pushChangeHandler(FlipChangeHandler(FlipDirection.RIGHT))
                    .popChangeHandler(FlipChangeHandler(FlipDirection.LEFT)))

            true
        }

        layoutManager = LinearLayoutManager(activity)

        recyclerView = view.recycler_view
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadFactions()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onFactionsLoaded(factions: List<Faction>) {
        factions.forEach { faction ->
            val item: FactionSelectItem = FactionSelectItem()
            item.factionName = faction.name
            item.factionDescription = faction.effect
            item.backgroundUrl = faction.iconUrl
            item.leaders = faction.leaders
            adapter?.add(item)
        }

        adapter?.notifyDataSetChanged()
    }

    override fun onFactionSelected(factionId: Int) {
        Log.d(TAG, "onFactionSelected() id: $factionId")
        TODO("Not yet implemented.")
    }

    override fun onLeaderSelected(cardId: Int) {
        Log.d(TAG, "onLeaderSelected() id: $cardId")
        TODO("Not yet implemented.")
    }

    override fun onLeaderConfirmed() {
        TODO("Not yet implemented.")
    }

}