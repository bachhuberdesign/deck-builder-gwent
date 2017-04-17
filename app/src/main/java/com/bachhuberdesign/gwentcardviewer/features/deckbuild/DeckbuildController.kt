package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.cardviewer.CardFilters
import com.bachhuberdesign.gwentcardviewer.features.cardviewer.CardViewerController
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Lane
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.SlideInChangeHandler
import com.bachhuberdesign.gwentcardviewer.util.dpToPx
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.controller_deckbuild.view.*
import javax.inject.Inject


/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckbuildController : Controller, DeckbuildMvpContract {

    constructor(deckId: Int) : super() {
        this.deckId = deckId
    }

    constructor(args: Bundle) : super()

    companion object {
        @JvmStatic val TAG: String = DeckbuildController::class.java.name
    }

    @Inject
    lateinit var presenter: DeckbuildPresenter

    lateinit var childRouter: Router

    private var deckId: Int = 0
    private var factionId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deckbuild)

        childRouter = getChildRouter(container).setPopsLastView(true)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (deckId == 0) {
            deckId = args.getInt("deckId")
        }

        view.show_card_viewer_button.setOnClickListener {
            showCardPicker()
        }

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadUserDeck(deckId)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("deckId", deckId)
        super.onSaveInstanceState(outState)
    }

    private fun showCardPicker() {
        val filters = CardFilters(filterByFactions = Pair(true, factionId))

        if (!childRouter.hasRootController()) {
            childRouter.setRoot(RouterTransaction.with(CardViewerController(filters, deckId))
                    .pushChangeHandler(SlideInChangeHandler(500, true))
                    .popChangeHandler(SlideInChangeHandler(500, true)))
        }
    }

    /**
     * Wrapper function which calls [DeckbuildController]'s presenter.addCardToDeck(int).
     *
     * Callback function onCardAdded() will be called by the presenter if the card is persisted successfully.
     */
    fun addCardToCurrentDeck(card: Card) {
        presenter.addCardToDeck(card, deckId)
        childRouters.forEach { router ->
            if (router.backstackSize > 0) {
                router.popCurrentController()
            }
        }
    }

    override fun onCardAdded(card: Card) {
        Toast.makeText(activity, "Card ${card.cardId} added", Toast.LENGTH_LONG).show()
        Log.d(TAG, "onCardAdded() " + card.toString())
    }

    override fun onCardRemoved() {
        Log.d(TAG, "onCardRemoved()")
    }

    override fun onDeckDeleted(deckId: Int) {
        Log.d(TAG, "onDeckDeleted()")
    }

    override fun onDeckLoaded(deck: Deck) {
        Log.d(TAG, "Deck: ${deck.name}, id: ${deck.id}, favorited: ${deck.isFavorited}, " +
                "created on: ${deck.createdDate}, last updated: ${deck.lastUpdate}")

        deck.cards.forEach { card -> Log.d(TAG, "Card ${card.name} loaded") }

        factionId = deck.faction

        view!!.faction_name_text.text = activity!!.getStringResourceByName(Faction.ID_TO_KEY.apply(deck.faction))
        view!!.leader_name_text.text = deck.cards[0].name
    }

    override fun onErrorLoadingDeck(message: String) {
        Log.e(TAG, "onErrorLoadingDeck: $message")
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showCardsByLane(cards: List<Card>, lane: Int) {
        if (cards.isEmpty()) {
            Log.d(TAG, "No cards available for lane $lane.")
            return
        }

        val layout: RelativeLayout?

        when (lane) {
            Lane.MELEE -> layout = view!!.melee_image_holder
            Lane.RANGED -> layout = view!!.ranged_image_holder
            Lane.SIEGE -> layout = view!!.siege_image_holder
            Lane.EVENT -> layout = view!!.event_image_holder
            else -> throw IndexOutOfBoundsException("Expected ${Lane.MELEE}, ${Lane.RANGED}, " +
                    "${Lane.SIEGE}, or ${Lane.EVENT}. Actual value received: $lane.")
        }

        var previousTag: Int = 0

        cards.forEachIndexed { index, card ->
            Log.d(TAG, "Adding card ${card.name} to lane $lane layout.")
            if (card.selectedLane == lane) {
                // Create view to hold card image
                val imageView: ImageView = ImageView(activity)
                imageView.id = View.generateViewId()
                imageView.tag = index + 42

                // Set LayoutParams for view
                val params = RelativeLayout.LayoutParams(activity!!.dpToPx(75).toInt(), WRAP_CONTENT)
                params.setMargins(0, 8, 0, 8)

                // Position view based on previous cards
                if (previousTag == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_START)
                } else {
                    params.addRule(RelativeLayout.END_OF, layout.findViewWithTag(previousTag).id)
                }

                // Add view and load image
                layout!!.addView(imageView, params)

                Glide.with(activity)
                        .load(card.iconUrl)
                        .fitCenter()
                        .into(imageView)

                previousTag = imageView.tag as Int
            }
        }
    }

}