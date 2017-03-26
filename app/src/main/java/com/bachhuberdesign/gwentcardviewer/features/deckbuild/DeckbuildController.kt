package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.gwentcardviewer.R
import com.bluelinelabs.conductor.Controller

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckbuildController : Controller(), DeckbuildMvpContract {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    // TODO: Inject dependencies

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view: View = inflater.inflate(R.layout.controller_deckbuild, container, false)

        return view
    }

    override fun onDeckCreated() {
        Log.d(TAG, "onDeckCreated()")
    }

    override fun onCardAdded() {
        Log.d(TAG, "onCardAdded()")
    }

    override fun onCardRemoved() {
        Log.d(TAG, "onCardRemoved()")
    }

    override fun onExportDecklist() {
        Log.d(TAG, "onExportDecklist()")
    }

    override fun onDeckDeleted(deckId: Int) {
        Log.d(TAG, "onDeckDeleted()")
    }

    override fun onDecksLoaded(decks: List<Deck>) {
        Log.d(TAG, "Deck list loaded.")
    }

}