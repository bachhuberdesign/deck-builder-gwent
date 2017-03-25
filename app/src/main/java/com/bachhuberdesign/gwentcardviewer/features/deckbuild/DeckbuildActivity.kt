package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.os.Bundle
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.BaseActivity

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckbuildActivity : BaseActivity() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_deckbuild)
    }

}