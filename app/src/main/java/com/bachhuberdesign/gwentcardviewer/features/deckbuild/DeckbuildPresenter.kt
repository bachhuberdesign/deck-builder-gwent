package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.CardType
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Lane
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import rx.Subscription
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
@Inject constructor(private val deckRepository: DeckRepository) : BasePresenter<DeckbuildMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckbuildPresenter::class.java.name
    }

    var cardSubscription: Subscription? = null
    var cardsToAnimate: MutableList<Card> = ArrayList()

    override fun detach() {
        super.detach()
        if (cardSubscription != null) {
            cardSubscription!!.unsubscribe()
        }

        if (cardsToAnimate.isNotEmpty()) {
            cardsToAnimate.clear()
        }
    }

    /**
     *
     */
    fun subscribeToCardUpdates(deckId: Int) {
        cardSubscription = deckRepository.observeCardUpdates(deckId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ cards ->
                    if (cards.isNotEmpty() && isViewAttached()) {
                        Log.d(TAG, "Adding card to animation cache: ${cards.last().name}, lane: ${cards.last().lane}, selectedLane: ${cards.last().selectedLane}")
                        cardsToAnimate.add(cards.last())
                        view!!.onCardAdded(cards.last())
                    }
                }, { error ->
                    Log.e(TAG, "Error querying cards for deck $deckId", error)
                })
    }

    /**
     *
     */
    fun loadCardsToAnimate() {
        if (cardsToAnimate.isEmpty()) {
            Log.d(TAG, "loadCardsToAnimate(): No cards need to be added to view.")
        } else if (isViewAttached()) {
            view!!.animateCards(cardsToAnimate)
            cardsToAnimate.clear()
        }
    }

    /**
     *
     */
    fun loadUserDeck(deckId: Int) {
        val deck: Deck? = deckRepository.getDeckById(deckId)

        if (deck == null && isViewAttached()) {
            view!!.onErrorLoadingDeck("Error loading deck $deckId")
        } else if (deck != null && isViewAttached()) {
            view!!.onDeckLoaded(deck)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.MELEE), Lane.MELEE)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.RANGED), Lane.RANGED)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.SIEGE), Lane.SIEGE)
            view!!.showCardsByLane(filterCardsByLane(deck, Lane.EVENT), Lane.EVENT)
            refreshTotals(deckId)
        }
    }

    /**
     *
     */
    private fun filterCardsByLane(deck: Deck, lane: Int): List<Card> {
        return deck.cards
                .filterNot { it.cardType == CardType.LEADER }
                .filter { it.selectedLane == lane }
    }

    /**
     *
     */
    fun addCardToDeck(card: Card, deckId: Int) {
        deckRepository.addCardToDeck(card, deckId)

        refreshTotals(deckId)
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

    /**
     *
     */
    private fun refreshTotals(deckId: Int) {
        val cards = deckRepository.getCardsForDeck(deckId)

        val meleeTotal = cards.filter { it.selectedLane == Lane.MELEE }.sumBy { it.power }
        val rangedTotal = cards.filter { it.selectedLane == Lane.RANGED }.sumBy { it.power }
        val siegeTotal = cards.filter { it.selectedLane == Lane.SIEGE }.sumBy { it.power }
        val eventTotal = cards.filter { it.selectedLane == Lane.EVENT }.sumBy { it.power }

        if (isViewAttached()) {
            val totals = LaneTotals(meleeTotal = meleeTotal, rangedTotal = rangedTotal, siegeTotal = siegeTotal, eventTotal = eventTotal)
            view!!.deckTotalsUpdated(totals)
        }
    }

    /**
     *
     */
    fun renameDeck(newDeckName: String, deckId: Int) {
        Log.d(TAG, "renameDeck(): newDeckName: $newDeckName, deckId: $deckId")

        deckRepository.renameDeck(newDeckName, deckId)
        if (isViewAttached()) {
            view!!.onDeckRenamed(newDeckName)
        }
    }

    /**
     *
     */
    fun onRenameButtonClicked(deckId: Int) {
        val deckName = deckRepository.getDeckById(deckId)?.name
        if (isViewAttached()) {
            view!!.showDeckRenameDialog(deckName!!)
        }
    }

}