package com.bachhuberdesign.deckbuildergwent

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.bachhuberdesign.MainMvpContract
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.Deck
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckbuildController
import com.bachhuberdesign.deckbuildergwent.features.deckselect.DeckSelectController
import com.bachhuberdesign.deckbuildergwent.features.factionselect.FactionSelectController
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BaseActivity
import com.bachhuberdesign.deckbuildergwent.util.DeckDrawerItem
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class MainActivity : BaseActivity(), MainMvpContract {

    companion object {
        @JvmStatic val TAG: String = MainActivity::class.java.name
    }

    @Inject lateinit var presenter: MainPresenter

    private lateinit var router: Router

    private var result: Drawer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent.inject(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(FactionSelectController()))
        }

        initNavigationDrawer()

        presenter.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.detach()
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun initNavigationDrawer() {
        val newDeck = PrimaryDrawerItem().withIdentifier(1).withName("New Deck")
        val editDeck = PrimaryDrawerItem().withIdentifier(2).withName("Edit Deck")
        val export = PrimaryDrawerItem().withIdentifier(3).withName("Export")
        val settings = SecondaryDrawerItem().withIdentifier(4).withName("Settings")
        val deckSelect = PrimaryDrawerItem().withIdentifier(5).withName("Deck List")

        val deckItem1 = DeckDrawerItem().withDeckName("Deck 1").withBackgroundUrl("file:///android_asset/leader-slim.png")
        val deckItem2 = DeckDrawerItem().withDeckName("Deck 2").withBackgroundUrl("file:///android_asset/leader-slim-2.png")
        val deckItem3 = DeckDrawerItem().withDeckName("Deck 3").withBackgroundUrl("file:///android_asset/leader-slim-3.png")

        result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withOnDrawerNavigationListener({ view ->
                    router.handleBack()
                    true
                })
                .addDrawerItems(
                        newDeck,
                        editDeck,
                        deckSelect,
                        export,
                        DividerDrawerItem(),
                        deckItem1,
                        deckItem2,
                        deckItem3,
                        settings
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    when (drawerItem.identifier.toInt()) {
                        1 -> router.pushController(RouterTransaction.with(FactionSelectController()))
                        2 -> router.pushController(RouterTransaction.with(DeckbuildController(1)))
                        3 -> Log.d(TAG, "Export item selected but not yet implemented.")
                        4 -> Log.d(TAG, "Settings item selected but not yet implemented.")
                        5 -> router.pushController(RouterTransaction.with(DeckSelectController()))
                    }
                    false
                }
                .build()
    }

    fun displayHomeAsUp(isEnabled: Boolean) {
        if (isEnabled) {
            result?.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            result?.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        }
    }

    override fun showRecentDecksInDrawer(decks: Map<String, Deck>) {
        Log.d(TAG, "showRecentDecksInDrawer() ")
    }

}