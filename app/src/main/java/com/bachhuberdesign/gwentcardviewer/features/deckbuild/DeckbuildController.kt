package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckbuildController : Controller(), DeckbuildMvpContract {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    @Inject
    lateinit var presenter: DeckbuildPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deckbuild)

        val persistedComponent = (activity as DeckbuildActivity)
                .persistedComponent.activitySubcomponent(ActivityModule(activity as DeckbuildActivity))
        persistedComponent.inject(this)

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadUserDecks()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
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

    override fun onDecksLoaded(decks: List<Deck>) {
        decks.forEach { deck ->
            Log.d(TAG, "Deck: ${deck.name}, id: ${deck.id}, favorited: ${deck.isFavorited}, " +
                    "created on: ${deck.createdDate}, last updated: ${deck.lastUpdate}")
        }
    }

}