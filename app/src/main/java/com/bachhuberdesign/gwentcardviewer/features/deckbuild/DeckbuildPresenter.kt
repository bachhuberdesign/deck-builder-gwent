package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
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

    /**
     *
     */
    fun loadUserDeck(deckId: Int) {
        Log.d(TAG, "Loading deck $deckId")
        var deck: Deck? = null
        val deckCursor = repository.getDeckById(deckId)
        val cardCursor = repository.getCardsForDeck(deckId)

        deckCursor.use {
            while (deckCursor.moveToNext()) {
                deck = Deck.MAPPER.apply(deckCursor)
            }
        }

        cardCursor.use {
            while (cardCursor.moveToNext()) {
                deck!!.cards.add(Card.MAPPER.apply(cardCursor))
            }
        }

        if (isViewAttached()) {
            view!!.onDeckLoaded(deck!!)
        }

//        cardRepository.getCardsForDeck(deckId)
//                .subscribeOn(Schedulers.io())
//                .subscribe({ query ->
//                    val cards: MutableList<Card> = ArrayList()
//                    val cardCursor = query.run()
//                    while (cardCursor!!.moveToNext()) {
//                        cards.add(Card.MAPPER.apply(cardCursor))
//                    }
//                    if (isViewAttached()) {
//                        view!!.onDeckLoaded(deck)
//                    }
//                }, { error ->
//                    Log.e(TAG, "Error getting cards for deck $deckId.", error)
//                }, { Log.d(TAG, "getCardsForDeck(): onComplete() called.") })
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

    /**
     *
     */
    fun addCard(cardId: Int) {
        // TODO:
    }

    /**
     *
     */
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