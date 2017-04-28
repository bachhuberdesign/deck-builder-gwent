package com.bachhuberdesign.deckbuildergwent.features.deckselect

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.shared.exception.CardException
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckbuildController
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.controller_deck_select.view.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckSelectController : Controller(), DeckSelectMvpContract {

    companion object {
        @JvmStatic val TAG: String = DeckSelectController::class.java.name
    }

    @Inject
    lateinit var presenter: DeckSelectPresenter

    var recyclerView: RecyclerView? = null
    var adapter: FastItemAdapter<DeckItem>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deck_select)

        activity?.title = "Deck List"

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        adapter = FastItemAdapter()
        adapter!!.withOnClickListener({ view, adapter, item, position ->
            if (item.deckId == 0) {
                CardException("Deck ID not set")
            } else {
                router.pushController(RouterTransaction.with(DeckbuildController(item.deckId)))
            }
            true
        })

        val layoutManager = LinearLayoutManager(activity)

        recyclerView = view.recycler_view
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadDeckList()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onDecksLoaded(decks: List<Deck>) {
        decks.forEach { deck ->
            Log.d(TAG, "Deck: ${deck.name}, id: ${deck.id}, favorited: ${deck.isFavorited}, " +
                    "created on: ${deck.createdDate}, last updated: ${deck.lastUpdate}")

            val item: DeckItem = DeckItem()
            item.deckName = deck.name
            item.factionId = deck.faction
            item.deckId = deck.id

            deck.cards.forEach { card ->
                if (card.cardType == CardType.LEADER) {
                    item.leaderName = card.name
                }
            }

            adapter?.add(item)
        }

        adapter?.notifyDataSetChanged()
    }

    override fun onNoDecksAvailable() {
        Log.d(TAG, "onNoDecksAvailable()")
    }

}