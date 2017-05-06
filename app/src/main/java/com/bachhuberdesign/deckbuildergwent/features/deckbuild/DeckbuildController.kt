package com.bachhuberdesign.deckbuildergwent.features.deckbuild

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardFilters
import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardViewerController
import com.bachhuberdesign.deckbuildergwent.features.deckcardlist.DeckDetailController
import com.bachhuberdesign.deckbuildergwent.features.factionselect.FactionSelectController
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Lane
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.*
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.animation.ViewAnimationFactory
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
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

    private lateinit var childRouter: Router

    private var animationDisposable: Disposable? = null
    private var deckId: Int = 0
    private var factionId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deckbuild)

        activity?.title = "Deckbuild Controller"
        setHasOptionsMenu(true)

        childRouter = getChildRouter(container).setPopsLastView(true)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (deckId == 0) {
            deckId = args.getInt("deckId")
        }

        view.show_card_viewer_button.background = null
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
        animationDisposable?.dispose()
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("deckId", deckId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_deckbuild, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_deck -> confirmDeckDeletion()
            R.id.menu_rename_deck -> presenter.onRenameButtonClicked(deckId)
            R.id.menu_deck_details -> showDeckDetails(deckId)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeckDetails(deckId: Int) {
        router.pushController(RouterTransaction.with(DeckDetailController(deckId))
                .pushChangeHandler(FlipChangeHandler())
                .popChangeHandler(FlipChangeHandler()))
    }

    private fun showCardPicker() {
        val filters = CardFilters(filterByFactions = Pair(true, factionId))

        view!!.show_card_viewer_button.clearAnimation()

        val cardFlip = ObjectAnimator.ofFloat(view!!.show_card_viewer_button, "rotationY", 360.0f)
        cardFlip.interpolator = OvershootInterpolator()
        cardFlip.duration = 725
        cardFlip.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!childRouter.hasRootController()) {
                    childRouter.setRoot(RouterTransaction.with(CardViewerController(filters, deckId))
                            .pushChangeHandler(SlideInChangeHandler(350, true))
                            .popChangeHandler(SlideInChangeHandler(350, true)))
                }
                Handler().postDelayed({
                    view!!.show_card_viewer_button.rotationY = 0.0f
                }, 500)
            }
        })

        cardFlip.start()
    }

    private fun confirmDeckDeletion() {
        MaterialDialog.Builder(activity!!)
                .title("Confirm Deletion")
                .content("Are you sure you want to delete this deck? This cannot be un-done.")
                .positiveText(android.R.string.ok)
                .onPositive({ dialog, action -> presenter.deleteDeck(deckId) })
                .negativeText(android.R.string.cancel)
                .show()
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
    fun removeCardFromDeck(card: Card) {
        presenter.removeCardFromDeck(card, deckId)
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

        (activity as MainActivity).displayHomeAsUp(false)

        // Delay to wait for pop animation to finish
        Maybe.empty<Any>()
                .delay(150, MILLISECONDS)
                .doOnComplete {
                    presenter.loadCardsToAnimate()
                }
                .subscribe()
    }

    override fun onDeckLoaded(deck: Deck) {
        activity?.title = deck.name

        factionId = deck.faction

        Glide.with(activity!!)
                .load(Uri.parse("file:///android_asset/faction_back_${deck.faction}_thumbnail.png"))
                .fitCenter()
                .dontAnimate()
                .into(view?.show_card_viewer_button)

        view!!.faction_name_text.text = activity!!.getStringResourceByName(Faction.ID_TO_KEY.apply(deck.faction))
        view!!.leader_name_text.text = deck.leader?.name
    }

    override fun onDeckDeleted(deckId: Int) {
        router.setRoot(RouterTransaction.with(FactionSelectController())
                .popChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.LEFT))
                .pushChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.RIGHT)))
        router.popToRoot()
    }

    override fun onErrorLoadingDeck(message: String) {
        Log.e(TAG, "onErrorLoadingDeck: $message")
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun animateCards(cardsToAnimate: List<Card>) {
        // Create Observable<List<Card>>, flatten to Observable<Card>, and zip with
        // Observable.interval for a delay between iterations
        Observable.fromArray(cardsToAnimate)
                .flatMapIterable { cards -> cards }
                .zipWith(Observable.interval(225, MILLISECONDS), BiFunction<Card, Long, Card> { card, delay -> card })
                .subscribe { card ->
                    // TODO: Simplify and document
                    if (view == null) {
                        return@subscribe
                    }

                    val layout: LinearLayout?

                    when (card.selectedLane) {
                        Lane.MELEE -> layout = view!!.melee_image_holder
                        Lane.RANGED -> layout = view!!.ranged_image_holder
                        Lane.SIEGE -> layout = view!!.siege_image_holder
                        Lane.EVENT -> layout = view!!.event_image_holder
                        else -> throw IndexOutOfBoundsException("Expected ${Lane.MELEE}, ${Lane.RANGED}, " +
                                "${Lane.SIEGE}, or ${Lane.EVENT}. Actual value received: ${card.selectedLane}.")
                    }

                    if (card.animationType == Card.ANIMATION_REMOVE) {
                        val viewToAnimate = layout?.findViewWithTag(card.cardId.toString())
                        viewToAnimate?.tag = "removing"

                        activity!!.runOnUiThread {
                            layout?.removeView(viewToAnimate)

                            if (layout.childCount > 0) {
                                Log.d(TAG, "Updating params")
                                val imageViewParams = LinearLayout.LayoutParams(activity!!.dpToPx(75).toInt(), WRAP_CONTENT)
                                imageViewParams.setMargins(activity!!.dpToPx(0).toInt(), 0, activity!!.dpToPx(0).toInt(), 0)
                                val ree = layout?.getChildAt(0)
                                ree?.layoutParams = imageViewParams
                            }

                        }
                    } else if (card.animationType == Card.ANIMATION_ADD) {
                        val imageView: ImageView = ImageView(activity)
                        imageView.id = View.generateViewId()
                        imageView.tag = card.cardId.toString()

                        val imageViewParams = LinearLayout.LayoutParams(activity!!.dpToPx(75).toInt(), WRAP_CONTENT)

                        if (layout.childCount > 0) {
                            imageViewParams.setMargins(activity!!.dpToPx(-28).toInt(), 0, activity!!.dpToPx(0).toInt(), 0)
                        }

                        // Load image and animate
                        activity!!.runOnUiThread {
                            layout!!.addView(imageView, imageViewParams)

                            Glide.with(activity)
                                    .load(Uri.parse("file:///android_asset/cards/${card.iconUrl}"))
                                    .animate(object : ViewAnimationFactory<GlideDrawable>(activity!!, R.anim.slide_right) {
                                        override fun build(isFromMemoryCache: Boolean, isFirstResource: Boolean): GlideAnimation<GlideDrawable> {
                                            return super.build(false, isFirstResource)
                                        }
                                    })
                                    .fitCenter()
                                    .into(imageView)
                        }
                    }
                }
    }

    override fun deckTotalsUpdated(totals: LaneTotals) {
        Log.d(TAG, "deckTotalsUpdated(): $totals")
        view!!.melee_lane_power_text.text = totals.meleeTotal.toString()
        view!!.ranged_lane_power_text.text = totals.rangedTotal.toString()
        view!!.siege_lane_power_text.text = totals.siegeTotal.toString()

        val totalPower: Int = totals.meleeTotal + totals.rangedTotal + totals.siegeTotal
        view!!.deck_power_total_text.text = totalPower.toString()
    }

    override fun showDeckRenameDialog(currentDeckName: String) {
        MaterialDialog.Builder(activity!!)
                .title("Rename Deck")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter a new name for your deck", currentDeckName, { dialog, input ->
                })
                .positiveText(R.string.confirm)
                .onPositive { dialog, which ->
                    val newDeckName = dialog.inputEditText?.text.toString().trim()
                    if (currentDeckName != newDeckName) {
                        presenter.renameDeck(newDeckName, deckId)
                    }
                }
                .negativeText(android.R.string.cancel)
                .show()
    }

    override fun onDeckRenamed(newDeckName: String) {
        activity?.title = newDeckName
    }

}