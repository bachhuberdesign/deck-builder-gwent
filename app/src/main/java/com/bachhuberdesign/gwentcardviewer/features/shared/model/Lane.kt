package com.bachhuberdesign.gwentcardviewer.features.shared.model

import io.reactivex.functions.Function

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class Lane {

    companion object {
        const val EVENT: Int = 1
        const val MELEE: Int = 2
        const val SIEGE: Int = 3
        const val RANGED: Int = 4
        const val MELEE_RANGED = 5
        const val MELEE_SIEGE = 6
        const val RANGED_SIEGE = 7
        const val MELEE_RANGED_SIEGE = 8
        const val ALL: Int = 99

        val ID_TO_KEY = Function<Int, String> { laneId ->
            when (laneId) {
                EVENT -> "event"
                MELEE -> "melee"
                SIEGE -> "siege"
                RANGED -> "ranged"
                else -> throw IndexOutOfBoundsException("laneId value was $laneId, expected 1-4.")
            }
        }
    }

}