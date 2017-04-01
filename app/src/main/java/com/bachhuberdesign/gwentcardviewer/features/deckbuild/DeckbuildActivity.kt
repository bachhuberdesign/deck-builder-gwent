package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.os.Bundle
import android.util.Log
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.factionselect.FactionSelectController
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BaseActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_deckbuild.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckbuildActivity : BaseActivity() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_deckbuild)

        router = Conductor.attachRouter(this, container, savedInstanceState)

        if (!router.hasRootController()) {
            Log.d(TAG, "Setting root controller")

            router.setRoot(RouterTransaction.with(FactionSelectController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

}