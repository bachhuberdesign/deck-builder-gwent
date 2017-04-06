package com.bachhuberdesign.gwentcardviewer.features.cardviewer

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
class CardViewerPresenter
@Inject constructor(private val repository: CardRepository) : BasePresenter<CardViewerMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    fun getUsableCards(factionId: Int) {
        Log.d(TAG, "Getting usable cards for faction $factionId")
        val cursor = repository.getAllFactionAndNeutralCards(factionId)
        val cards: MutableList<Card> = ArrayList()

        cursor.use {
            Log.d(TAG, "getUsableCards(): Cursor has ${cursor.count} rows.")
            while (cursor.moveToNext()) {
                cards.add(Card.MAPPER.apply(cursor))
            }
        }

        if (isViewAttached()) {
            view!!.onCardsLoaded(cards)
        }
    }

}

