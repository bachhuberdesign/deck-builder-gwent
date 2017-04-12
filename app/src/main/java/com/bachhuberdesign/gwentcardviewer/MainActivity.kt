package com.bachhuberdesign.gwentcardviewer

import android.os.Bundle
import android.util.Log
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckbuildController
import com.bachhuberdesign.gwentcardviewer.features.factionselect.FactionSelectController
import com.bachhuberdesign.gwentcardviewer.features.shared.base.BaseActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class MainActivity : BaseActivity() {

    companion object {
        @JvmStatic val TAG: String = MainActivity::class.java.name
    }

    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        router = Conductor.attachRouter(this, container, savedInstanceState)

        if (!router.hasRootController()) {
            Log.d(TAG, "Setting root controller")

            // TODO: Show drawer

            if (!BuildConfig.DEBUG) {
                router.setRoot(RouterTransaction.with(DeckbuildController(1)))
            } else {
                router.setRoot(RouterTransaction.with(FactionSelectController()))
            }
        }

        initNavigationDrawer()
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    private fun initNavigationDrawer() {
        val newDeck = PrimaryDrawerItem().withIdentifier(1).withName("New Deck")
        val editDeck = PrimaryDrawerItem().withIdentifier(2).withName("Edit Deck")
        val export = PrimaryDrawerItem().withIdentifier(3).withName("Export")
        val settings = SecondaryDrawerItem().withIdentifier(4).withName("Settings")

        val result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        newDeck,
                        editDeck,
                        export,
                        DividerDrawerItem(),
                        settings
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    when (drawerItem.identifier.toInt()) {
                        1 -> router.pushController(RouterTransaction.with(FactionSelectController()))
                        2 -> router.pushController(RouterTransaction.with(DeckbuildController(1)))
                        3 -> Log.d(TAG, "Export item selected but not yet implemented.")
                        4 -> Log.d(TAG, "Settings item selected but not yet implemented.")
                    }
                    false
                }
                .build()
    }

}