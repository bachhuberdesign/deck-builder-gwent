package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class CardDetailPresenter
@Inject constructor(private val cardRepository: CardRepository) : BasePresenter<CardDetailMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = CardDetailPresenter::class.java.name
    }

    fun loadCard(cardId: Int) {
        val card = cardRepository.getCardById(cardId)

        getViewOrThrow().onCardLoaded(card)
    }

}