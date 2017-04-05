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

    fun loadUserDeck(deckId: Int) {
        Log.d(TAG, "Loading deck $deckId")
        val cursor = repository.getDeckById(deckId)
        var deck: Deck? = null

        cursor.use {
            while (cursor.moveToNext()) {
                deck = Deck.MAPPER.apply(cursor)
            }
        }

        if (isViewAttached()) {
            view!!.onDeckLoaded(deck)
        }
    }

    /**
     *
     */
    fun loadAllUserDecks() {
        Log.d(TAG, "Loading user decks.")
        val decks: MutableList<Deck> = ArrayList()
        val query = repository.getAllUserCreatedDecks()

        query.subscribe({ query ->
            val cursor = query.run()

            cursor.use {
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

    fun addCard(cardId: Int) {
        // TODO:
    }

    fun removeCard(cardId: Int) {
        // TODO:
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