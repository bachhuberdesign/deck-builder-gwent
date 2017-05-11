package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import com.bachhuberdesign.deckbuildergwent.features.shared.base.MvpContract
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
interface CardDetailMvpContract : MvpContract {

    fun onCardLoaded(card: Card)

}
