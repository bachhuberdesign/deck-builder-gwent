package com.bachhuberdesign.deckbuildergwent.features.factionselect

import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckbuildController
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.changehandler.FlipChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.getStringResourceByName
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.controller_leader_confirm.view.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class LeaderConfirmController : Controller, LeaderConfirmMvpContract {

    constructor(card: Card) : super() {
        this.card = card
    }

    constructor(args: Bundle) : super()

    companion object {
        val TAG: String = LeaderConfirmController::class.java.name
    }

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var presenter: LeaderConfirmPresenter

    var card: Card? = null
    var defaultDeckName = "New Deck"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_leader_confirm)
        activity?.title = "Select Leader"

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (card == null) {
            card = gson.fromJson(args.getString("card"), Card::class.java)
        }

        Glide.with(activity!!)
                .load(Uri.parse("file:///android_asset/cards/${card?.iconUrl}"))
                .dontAnimate()
                .into(view.leader_image)

        view.leader_name_text.text = card?.name
        view.leader_power_text.text = card?.description
        view.faction_name_text.text = activity!!.getStringResourceByName(Faction.ID_TO_KEY.apply(card?.faction))

        view.confirm_leader_button.setOnClickListener {
            showDeckCreateDialog()
        }

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        card?.let { presenter.loadDefaultDeckName(it) }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("card", gson.toJson(card))
        super.onSaveInstanceState(outState)
    }

    private fun showDeckCreateDialog() {
        MaterialDialog.Builder(activity!!)
                .title("Confirm Deck Creation")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter a name for your new deck", defaultDeckName, { dialog, input ->
                })
                .negativeText(android.R.string.cancel)
                .positiveText(R.string.create_deck)
                .onPositive { dialog, which ->
                    val deckName = dialog.inputEditText?.text.toString().trim()
                    presenter.saveNewDeck(deckName, card!!)
                }
                .show()
    }

    override fun onDeckSaved(deckId: Int) {
        router.setBackstack(arrayListOf(
                RouterTransaction.with(FactionSelectController())
                        .pushChangeHandler(FadeChangeHandler())
                        .popChangeHandler(FadeChangeHandler()),
                RouterTransaction.with(DeckbuildController(deckId))
                        .tag(DeckbuildController.TAG)
                        .popChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.LEFT))),
                FlipChangeHandler(FlipChangeHandler.FlipDirection.RIGHT))
    }

    override fun displayError(messageToDisplay: String) {
        Toast.makeText(activity, messageToDisplay, Toast.LENGTH_LONG).show()
    }

    override fun onDefaultDeckNameLoaded(deckName: String) {
        defaultDeckName = deckName
    }

}
