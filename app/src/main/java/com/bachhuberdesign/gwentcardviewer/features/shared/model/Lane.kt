package com.bachhuberdesign.gwentcardviewer.features.shared.model

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
    }

}