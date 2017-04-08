package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import com.bachhuberdesign.gwentcardviewer.features.shared.base.MvpContract
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface CardViewerMvpContract : MvpContract {

    fun onCardsLoaded(cards: List<Card>)

    fun onListFiltered(filteredCards: List<Card>)

    fun onCardChecked(card: Card)

}