package com.bachhuberdesign.deckbuildergwent.features.deckbuild

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
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
import com.bachhuberdesign.deckbuildergwent.features.deckdetail.DeckDetailController
import com.bachhuberdesign.deckbuildergwent.features.factionselect.FactionSelectController
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Lane
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.changehandler.FlipChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.changehandler.SlideInChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.dpToPx
import com.bachhuberdesign.deckbuildergwent.util.getStringResourceByName
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.animation.ViewAnimationFactory
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

    private var animationDisposable: Disposable? = null
    private var deckId: Int = 0
    private var factionId: Int = 0
    private var reloadDeck: Boolean = true
    private var deck: Deck? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deckbuild)

        activity?.title = "Deckbuild Controller"
        setHasOptionsMenu(true)

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

        retainViewMode = RetainViewMode.RETAIN_DETACH

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)

        if (reloadDeck) {
            // First attach or view was destroyed so deck should be re-loaded
            reloadDeck = false
            presenter.loadUserDeck(deckId)
        } else {
            presenter.loadCardsToAnimate(deck!!)
        }

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

    override fun onDestroyView(view: View) {
        reloadDeck = true
        presenter.unsubscribeToCardUpdates()
        animationDisposable?.dispose()

        super.onDestroyView(view)
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
                .tag(DeckDetailController.TAG)
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
                router.pushController(RouterTransaction.with(CardViewerController(filters, deckId))
                        .tag(CardViewerController.TAG)
                        .popChangeHandler(SlideInChangeHandler(300, false))
                        .pushChangeHandler(SlideInChangeHandler(300, true)))

                Handler().postDelayed({
                    view!!.show_card_viewer_button.rotationY = 0.0f
                }, 300)
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

    override fun onDeckLoaded(deck: Deck) {
        this.deck = deck
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

    override fun animateCards(cardsToAnimate: List<Card>, deck: Deck) {
        this.deck = deck

        // Create Observable<List<Card>>, flatten to Observable<Card>, and zip with
        // Observable.interval for a delay between iterations
        animationDisposable = Observable.fromArray(cardsToAnimate)
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

    override fun onLaneTotalsUpdated(totals: LaneTotals) {
        Log.d(TAG, "onLaneTotalsUpdated(): $totals")
        view!!.melee_lane_power_text.text = totals.meleeTotal.toString()
        view!!.ranged_lane_power_text.text = totals.rangedTotal.toString()
        view!!.siege_lane_power_text.text = totals.siegeTotal.toString()

        val totalPower: Int = totals.meleeTotal + totals.rangedTotal + totals.siegeTotal
        view!!.deck_power_total_text.text = totalPower.toString()

        // Create linear gradients for our lane power TextViews
        val goldShader: Shader = LinearGradient(0f, 0f, 0f, 100f, Color.parseColor("#f1c248"), Color.parseColor("#f08c0b"), Shader.TileMode.CLAMP)
        val silverShader: Shader = LinearGradient(0f, 0f, 0f, 100f, Color.parseColor("#bcbcbc"), Color.DKGRAY, Shader.TileMode.CLAMP)

        // Set the shader (overrides any other shader including drop-shadow)
        view!!.melee_lane_power_text.paint.shader = silverShader
        view!!.ranged_lane_power_text.paint.shader = silverShader
        view!!.siege_lane_power_text.paint.shader = silverShader
        view!!.deck_power_total_text.paint.shader = goldShader
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