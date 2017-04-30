package com.bachhuberdesign.deckbuildergwent

import android.content.Context
import android.os.Bundle
import android.widget.Toast
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
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class MainActivity : BaseActivity(), MainMvpContract {

    companion object {
        @JvmStatic val TAG: String = MainActivity::class.java.name
    }

    @Inject
    lateinit var presenter: MainPresenter

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
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        super.onPause()
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
        val newDeck = PrimaryDrawerItem().withIdentifier(1).withName("New Deck").withIcon(FontAwesome.Icon.faw_plus_circle)
        val deckSelect = PrimaryDrawerItem().withIdentifier(2).withName("Deck List").withIcon(CommunityMaterial.Icon.cmd_cards_outline)
        val export = PrimaryDrawerItem().withIdentifier(3).withName("Export").withIcon(CommunityMaterial.Icon.cmd_export)
        val winTrack = PrimaryDrawerItem().withIdentifier(4).withName("Stat Tracker").withIcon(FontAwesome.Icon.faw_pie_chart)
        val settings = SecondaryDrawerItem().withIdentifier(5).withName("Settings").withIcon(FontAwesome.Icon.faw_sliders)

        result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withOnDrawerNavigationListener({ view ->
                    router.handleBack()
                    true
                })
                .addDrawerItems(newDeck, deckSelect, export, winTrack, DividerDrawerItem())
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    when (drawerItem.identifier.toInt()) {
                        1 -> router.pushController(RouterTransaction.with(FactionSelectController()))
                        2 -> router.pushController(RouterTransaction.with(DeckSelectController()))
                        3 -> Toast.makeText(this, "Export not yet implemented.", Toast.LENGTH_LONG).show()
                        4 -> Toast.makeText(this, "Win tracking not yet implemented.", Toast.LENGTH_LONG).show()
                        5 -> Toast.makeText(this, "Settings not yet implemented.", Toast.LENGTH_LONG).show()
                        99 -> {
                            // Item is a recent deck so start a transaction to DeckbuildController
                            val deckId = (drawerItem as DeckDrawerItem).deckId
                            router.pushController(RouterTransaction.with(DeckbuildController(deckId)))
                        }
                    }
                    false
                }
                .build()

        result?.addStickyFooterItem(settings)
    }

    /**
     *
     */
    fun displayHomeAsUp(isEnabled: Boolean) {
        if (isEnabled) {
            result?.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            result?.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        }
    }

    override fun showRecentDecksInDrawer(decks: List<Deck>) {
        // Use removeItem() x5 as workaround for bug with removeItems()
        result?.removeItem(99)
        result?.removeItem(99)
        result?.removeItem(99)
        result?.removeItem(99)
        result?.removeItem(99)

        decks.forEachIndexed { index, deck ->
            val newItem = DeckDrawerItem()
                    .withDeckName(deck.name)
                    .withDeckId(deck.id)
                    .withBackgroundUrl("file:///android_asset/slim/${deck.leader?.iconUrl}")
                    .withIdentifier(99)
                    .withTag("deck$index")

            result?.addItem(newItem)
        }
    }

}