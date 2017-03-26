package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class Deck(var id: Int = 0,
                var name: String = "",
                var cards: MutableList<Card> = ArrayList(),
                var isFavorited: Boolean = false,
                var createdDate: Date = Date(),
                var lastUpdate: Date? = Date())