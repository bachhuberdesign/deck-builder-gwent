package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bumptech.glide.Glide
import io.reactivex.Maybe
import kotlinx.android.synthetic.main.controller_deckbuild.view.*
import java.util.concurrent.TimeUnit.MILLISECONDS
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

        activity?.title = "Deckbuild Controller"

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
        presenter.subscribeToCardUpdates(deckId)
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
    }

    /**
     *
     */
    fun closeCardViewerAndAnimate() {
        childRouters.forEach { router ->
            if (router.backstackSize > 0) {
                router.popCurrentController()
            }
        }

        // Delay to wait for pop animation to finish
        Maybe.empty<Any>()
                .delay(500, MILLISECONDS)
                .doOnComplete {
                    presenter.loadCardsToAnimate()
                }
                .subscribe()
    }

    override fun onCardAdded(card: Card) {
        Log.d(TAG, "onCardAdded() " + card.toString())
    }

    override fun onCardRemoved(card: Card) {
        Log.d(TAG, "onCardRemoved()")
    }

    override fun onDeckDeleted(deckId: Int) {
        Log.d(TAG, "onDeckDeleted()")
    }

    override fun onDeckLoaded(deck: Deck) {
        activity?.title = deck.name

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

        val guidelineLeft: Int = view!!.card_images_start_guideline.id
        var guidelineTop: Int? = null
        var guidelineBottom: Int? = null

        when (lane) {
            Lane.MELEE -> {
                guidelineTop = view!!.melee_top_guideline.id
                guidelineBottom = view!!.ranged_top_guideline.id
            }
            Lane.RANGED -> {
                guidelineTop = view!!.ranged_top_guideline.id
                guidelineBottom = view!!.siege_top_guideline.id
            }
            Lane.SIEGE -> {
                guidelineTop = view!!.siege_top_guideline.id
                guidelineBottom = view!!.event_top_guideline.id
            }
            Lane.EVENT -> {
                guidelineTop = view!!.event_top_guideline.id
                guidelineBottom = view!!.event_bottom_guideline.id
            }
            else -> throw IndexOutOfBoundsException("Expected ${Lane.MELEE}, ${Lane.RANGED}, " +
                    "${Lane.SIEGE}, or ${Lane.EVENT}. Actual value received: $lane.")
        }

        val views: MutableList<Int> = ArrayList()
        val weights: MutableList<Float> = ArrayList()

        cards.forEachIndexed { index, card ->
            val imageView: ImageView = ImageView(activity)
            imageView.id = View.generateViewId()

            view!!.constraint_layout.addView(imageView)
            views.add(imageView.id)
            weights.add(0.0f)
        }

        // Guideline 6 TOP, guideline 7 BOTTOM, guideline 5 LEFT, guideline 4 RIGHT

        var previousId: Int = 0
        val constraintSet = ConstraintSet()
        if (views.size < 2) {
            return
        }
        constraintSet.clone(view!!.constraint_layout)
        constraintSet.createHorizontalChain(views.first(), ConstraintSet.LEFT,
                views.last(), ConstraintSet.RIGHT,
                views.toIntArray(), null,
                ConstraintSet.CHAIN_PACKED)

        views.forEach { id ->
            constraintSet.constrainWidth(id, 150)
            constraintSet.constrainHeight(id, ConstraintSet.WRAP_CONTENT)
        }

        constraintSet.connect(views.first(), ConstraintSet.LEFT, guidelineLeft, ConstraintSet.LEFT, 0)
        constraintSet.connect(views.first(), ConstraintSet.TOP, guidelineTop, ConstraintSet.TOP, 0)
        constraintSet.connect(views.last(), ConstraintSet.TOP, guidelineTop, ConstraintSet.TOP, 0)

        activity!!.runOnUiThread {
            constraintSet.applyTo(view!!.constraint_layout)
        }

        views.forEachIndexed { index, id ->
            Log.d(TAG, "Icon URL to load: ${cards[index].iconUrl}")
            Glide.with(activity)
                    .load(cards[index].iconUrl)
                    .fitCenter()
                    .into(view!!.findViewById(id) as ImageView)
        }


//        var previousTag: Int = 0
//
//        cards.forEachIndexed { index, card ->
//            if (card.selectedLane == lane) {
//                // Create view to hold card image
//                val imageView: ImageView = ImageView(activity)
//                imageView.id = View.generateViewId()
//                imageView.tag = index + 42
//
//                // Set LayoutParams for view
//                val params = RelativeLayout.LayoutParams(activity!!.dpToPx(75).toInt(), WRAP_CONTENT)
//                params.setMargins(0, 8, 0, 8)
//
//                // Position view based on previous cards
//                if (previousTag == 0) {
//                    params.addRule(RelativeLayout.ALIGN_PARENT_START)
//                } else {
//                    params.addRule(RelativeLayout.END_OF, layout.findViewWithTag(previousTag).id)
//                }
//
//                // Add view and load image
//                layout!!.addView(imageView, params)
//
//                Glide.with(activity)
//                        .load(card.iconUrl)
//                        .fitCenter()
//                        .into(imageView)
//
//                previousTag = imageView.tag as Int
//            }
//        }
    }

    override fun animateCards(cardsToAnimate: List<Card>) {
//        // Guideline 6 TOP, guideline 7 BOTTOM, guideline 5 LEFT, guideline 4 RIGHT
//        val constraintSet = ConstraintSet()
//
//        constraintSet.clone(view!!.constraint_layout)
//
//        TransitionManager.beginDelayedTransition(view!!.constraint_layout)
//
//        constraintSet.centerVertically(R.id.deck_ranged_power_text, R.id.constraint_layout)
//        constraintSet.centerHorizontally(R.id.deck_ranged_power_text, R.id.constraint_layout)
//
//        activity!!.runOnUiThread {
//            constraintSet.applyTo(view!!.constraint_layout)
//        }
//
//        // Create Observable<List<Card>>, flatten to Observable<Card>, and zip with a delay for iteration
//        Observable.fromArray(cardsToAnimate)
//                .flatMapIterable { cards -> cards }
//                .zipWith(Observable.interval(500, MILLISECONDS), BiFunction<Card, Long, Card> { card, delay -> card })
//                .doOnComplete { Log.d(TAG, "Animated ${cardsToAnimate.size} cards.") }
//                .subscribe { card ->
//                    // TODO: Animate here
//                    Log.d(TAG, "Animating card ${card.cardId}, current time: ${System.currentTimeMillis()}")
//                }
    }

}

