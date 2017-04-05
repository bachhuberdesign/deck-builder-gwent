package com.bachhuberdesign.gwentcardviewer.features.cardviewer

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
class CardViewerPresenter
@Inject constructor(private val repository: CardRepository) : BasePresenter<CardViewerMvpContract>() {

    fun getUsableCards(factionId: Int) {
        val cursor = repository.getAllFactionAndNeutralCards(factionId)
        val cards: MutableList<Card> = ArrayList()

        cursor.use {
            while (cursor.moveToNext()) {
                cards.add(Card.MAPPER.apply(cursor))
            }
        }

        if (isViewAttached()) {
            view!!.onCardsLoaded(cards)
        }
    }

}

