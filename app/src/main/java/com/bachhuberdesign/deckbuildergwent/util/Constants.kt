package com.bachhuberdesign.deckbuildergwent.util

import android.graphics.Color

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class Constants {

    companion object {
        @JvmStatic val TAG: String = Constants::class.java.name

        @JvmStatic val FLAT_UI_COLORS: IntArray = intArrayOf(
                Color.rgb(155, 89, 182),
                Color.rgb(52, 152, 219),
                Color.rgb(149, 165, 166),
                Color.rgb(231, 76, 60),
//                Color.rgb(52, 73, 94),
                Color.rgb(46, 204, 113))
    }


}