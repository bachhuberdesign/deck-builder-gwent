package com.bachhuberdesign.deckbuildergwent.features.cardviewer

/**
 * Helper class which contains paired arguments used for filtering a list of [com.bachhuberdesign.deckbuildergwent.features.shared.model.Card].
 *
 * If the boolean in the pair is set to true, the corresponding Int ID should be set as it will be
 * used for filtering.
 *
 * Example: filterByDeck = Pair(true, deckId)
 *
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
data class CardFilters(
        var filterByDeck: Pair<Boolean, Int> = Pair(false, 0),
        var filterByFactions: Pair<Boolean, Int> = Pair(false, 0),
        var filterByRarity: Pair<Boolean, Int> = Pair(false, 0),
        var filterByCardType: Pair<Boolean, Int> = Pair(false, 0)
)
