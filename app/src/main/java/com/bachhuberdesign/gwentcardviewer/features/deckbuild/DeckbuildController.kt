package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.cardviewer.CardFilters
import com.bachhuberdesign.gwentcardviewer.features.cardviewer.CardViewerController
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.SlideInChangeHandler
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.controller_deckbuild.view.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckbuildController : Controller, DeckbuildMvpContract {

    constructor(deckId: Int) : super() {
        this.deckId = deckId
    }

    constructor(args: Bundle) : super()

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    @Inject
    lateinit var presenter: DeckbuildPresenter

    var deckId: Int = 0
    var factionId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deckbuild)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (deckId == 0) {
            deckId = args.getInt("deckId")
        }

        view.show_card_viewer_button.setOnClickListener { v ->
            showCardPicker()
        }

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadUserDeck(deckId)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("deckId", deckId)
        super.onSaveInstanceState(outState)
    }

    private fun showCardPicker() {
        val filters = CardFilters(filterByFactions = Pair(true, factionId))

        router.pushController(RouterTransaction.with(CardViewerController(filters, deckId))
                .pushChangeHandler(SlideInChangeHandler(500, true))
                .popChangeHandler(SlideInChangeHandler(500, false)))
    }

    override fun onCardAdded() {
        Log.d(TAG, "onCardAdded()")
    }

    override fun onCardRemoved() {
        Log.d(TAG, "onCardRemoved()")
    }

    override fun onDeckDeleted(deckId: Int) {
        Log.d(TAG, "onDeckDeleted()")
    }

    override fun onDeckLoaded(deck: Deck) {
        factionId = deck.faction
        Log.d(TAG, "Deck: ${deck.name}, id: ${deck.id}, favorited: ${deck.isFavorited}, " +
                "created on: ${deck.createdDate}, last updated: ${deck.lastUpdate}")

        deck.cards.forEach { card ->
            Log.d(TAG, "Card: ${card.name}")
        }

        view!!.faction_name_text.text = activity!!.getStringResourceByName(Faction.ID_TO_KEY.apply(deck.faction))
        view!!.leader_name_text.text = deck.cards[0].name
    }

    override fun onDecksLoaded(decks: List<Deck>) {
        decks.forEach { deck ->
            Log.d(TAG, "Deck: ${deck.name}, id: ${deck.id}, favorited: ${deck.isFavorited}, " +
                    "created on: ${deck.createdDate}, last updated: ${deck.lastUpdate}")
        }
    }

    override fun onErrorLoadingDeck(message: String) {
        Log.e(TAG, "onErrorLoadingDeck: $message")
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}