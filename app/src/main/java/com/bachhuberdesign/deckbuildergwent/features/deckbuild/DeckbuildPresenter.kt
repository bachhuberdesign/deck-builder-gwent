package com.bachhuberdesign.deckbuildergwent.features.deckbuild

import android.util.Log
import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Lane
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
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
@Inject constructor(private val deckRepository: DeckRepository,
                    private val cardRepository: CardRepository) : BasePresenter<DeckbuildMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = DeckbuildPresenter::class.java.name
    }

    var cardSubscription: Subscription? = null
    var addedCardsAnimationCache: MutableList<Card> = ArrayList()
    var removedCardsAnimationCache: MutableList<Card> = ArrayList()

    fun unsubscribeToCardUpdates() {
        Log.d(TAG, "unsubscribeToCardUpdates()")

        if (cardSubscription != null) {
            cardSubscription!!.unsubscribe()
        }

        addedCardsAnimationCache.clear()
        removedCardsAnimationCache.clear()
    }

    /**
     *
     */
    fun subscribeToCardUpdates(deckId: Int) {
        Log.d(TAG, "subscribeToCardUpdates()")

        if (cardSubscription == null) {
            var previousCards: MutableList<Card> = deckRepository.getCardsForDeck(deckId)

            cardSubscription = deckRepository.observeCardUpdates(deckId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ cards ->
                        if (cards.isNotEmpty() && isViewAttached() && cards.size > previousCards.size) {
                            val cardToAdd = cards.last()
                            cardToAdd.animationType = Card.ANIMATION_ADD

                            addedCardsAnimationCache.add(cardToAdd)
                            Log.d(TAG, "Added ${cardToAdd.name} to addedCardsAnimationCache")
                        } else if (cards.size < previousCards.size) {
                            cards.forEach { previousCards.remove(it) }

                            if (previousCards.firstOrNull() != null) {
                                val cardToRemove = previousCards.first()
                                cardToRemove.animationType = Card.ANIMATION_REMOVE

                                removedCardsAnimationCache.add(previousCards.first())
                                Log.d(TAG, "Added ${cardToRemove.name} to removedCardsAnimationCache")
                            }
                        }

                        previousCards = cards
                    }, { error ->
                        Log.e(TAG, "Error querying cards for deck $deckId", error)
                    })
        }
    }

    /**
     *
     */
    fun loadCardsToAnimate() {
        removedCardsAnimationCache.forEach { addedCardsAnimationCache.remove(it) }

        val cardsToAnimate = addedCardsAnimationCache
        cardsToAnimate.addAll(removedCardsAnimationCache)

        if (isViewAttached()) {
            view!!.animateCards(cardsToAnimate)
        }

        addedCardsAnimationCache.clear()
        removedCardsAnimationCache.clear()
    }

    /**
     *
     */
    fun loadUserDeck(deckId: Int) {
        val deck: Deck? = deckRepository.getDeckById(deckId)

        if (deck != null) {
            getViewOrThrow().onDeckLoaded(deck)
            refreshTotals(deckId)

            deck.cards.forEach { it.animationType = Card.ANIMATION_ADD }

            // Filter out leader card and animate all lanes simultaneously
            getViewOrThrow().animateCards(deck.cards.filterNot { it.cardType == CardType.LEADER }.filter { it.selectedLane == Lane.MELEE })
            getViewOrThrow().animateCards(deck.cards.filterNot { it.cardType == CardType.LEADER }.filter { it.selectedLane == Lane.RANGED })
            getViewOrThrow().animateCards(deck.cards.filterNot { it.cardType == CardType.LEADER }.filter { it.selectedLane == Lane.SIEGE })
            getViewOrThrow().animateCards(deck.cards.filterNot { it.cardType == CardType.LEADER }.filter { it.selectedLane == Lane.EVENT })
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

    /**
     *
     */
    private fun refreshTotals(deckId: Int) {
        val cards = deckRepository.getCardsForDeck(deckId)

        val meleeTotal = cards.filter { it.selectedLane == Lane.MELEE }.sumBy { it.power }
        val rangedTotal = cards.filter { it.selectedLane == Lane.RANGED }.sumBy { it.power }
        val siegeTotal = cards.filter { it.selectedLane == Lane.SIEGE }.sumBy { it.power }

        if (isViewAttached()) {
            val totals = LaneTotals(meleeTotal = meleeTotal, rangedTotal = rangedTotal, siegeTotal = siegeTotal)
            view!!.onLaneTotalsUpdated(totals)
        }

    }

    /**
     *
     */
    fun renameDeck(newDeckName: String, deckId: Int) {
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