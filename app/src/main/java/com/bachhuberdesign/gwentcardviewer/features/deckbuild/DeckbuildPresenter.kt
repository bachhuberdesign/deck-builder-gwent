package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.cardviewer.CardRepository
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Lane
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckbuildPresenter
@Inject constructor(private val deckRepository: DeckRepository,
                    private val cardRepository: CardRepository) : BasePresenter<DeckbuildMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckbuildPresenter::class.java.name
    }


    override fun attach(view: DeckbuildMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    /**
     *
     */
    fun loadUserDeck(deckId: Int) {
        val deck: Deck? = deckRepository.getDeckById(deckId)

        // TODO:
        deckRepository.queryCardsForDeck(deckId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ query ->
                    query.forEach { card ->
                        if (isViewAttached()) {
                            view!!.onCardAdded(card)
                        }
                    }
                }, { error ->
                    Log.e(TAG, "Error querying cards for deck $deckId", error)
                })

        if (deck == null && isViewAttached()) {
            view!!.onErrorLoadingDeck("Error loading deck $deckId")
        } else if (deck != null && isViewAttached()) {
            view!!.onDeckLoaded(deck)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.MELEE), Lane.MELEE)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.RANGED), Lane.RANGED)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.SIEGE), Lane.SIEGE)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.EVENT), Lane.EVENT)
        }
    }

    private fun filterCardsByLane(deck: Deck, lane: Int): List<Card> {
        val filteredList: MutableList<Card> = ArrayList()
        deck.cards.forEach { card ->
            if (card.selectedLane == lane) {
                Log.d(TAG, "Adding card ${card.name} to filtered list. selectedLane: ${card.selectedLane}, lane to filter to: $lane")
                filteredList.add(card)
            }
        }
        return filteredList
    }

    /**
     *
     */
    fun addCardToDeck(card: Card, deckId: Int) {
        deckRepository.addCardToDeck(card, deckId)
        if (isViewAttached()) {
            view!!.onCardAdded(card)
        }
    }

    /**
     *
     */
    fun removeCardFromDeck(cardId: Int) {
        Log.d(TAG, "removeCard() called for card $cardId")
        // TODO:

        if (isViewAttached()) {
            view!!.onCardRemoved()
        }
    }

    /**
     *
     */
    fun deleteDeck(deckId: Int) {
        deckRepository.deleteDeck(deckId)

        if (isViewAttached()) {
            view!!.onDeckDeleted(deckId)
        }
    }

}