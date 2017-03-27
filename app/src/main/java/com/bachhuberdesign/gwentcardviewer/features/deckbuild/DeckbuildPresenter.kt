package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckbuildPresenter
@Inject constructor(private val repository: DeckRepository) : BasePresenter<DeckbuildMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    override fun attach(view: DeckbuildMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    fun addCard(cardId: Int) {
        // TODO:
    }

    fun removeCard(cardId: Int) {
        // TODO:
    }

    /**
     *
     */
    fun loadUserDecks() {
        Log.d(TAG, "Loading user decks.")
        val decks: MutableList<Deck> = ArrayList()
        val query = repository.queryUserCreatedDecks()

        query.subscribe({ query ->
            val cursor = query.run()

            cursor.use {
                Log.d(TAG, "Iterating through cursor.")
                while (cursor!!.moveToNext()) {
                    decks.add(Deck.MAPPER.apply(cursor))
                }
            }

            if (decks.isNotEmpty() && isViewAttached()) {
                Log.d(TAG, "Deck list not empty & view attached")
                view!!.onDecksLoaded(decks)
            }
        })
    }

    /**
     *
     */
    fun saveDeck(deck: Deck) {
        repository.saveDeck(deck)
    }

    /**
     *
     */
    fun deleteDeck(deckId: Int) {
        repository.deleteDeck(deckId)

        if (isViewAttached()) {
            view!!.onDeckDeleted(deckId)
        }
    }

}