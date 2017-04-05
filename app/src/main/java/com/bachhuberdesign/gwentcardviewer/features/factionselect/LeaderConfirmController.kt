package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckbuildController
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.FlipChangeHandler
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
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
        val TAG: String = this::class.java.name
    }

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var presenter: LeaderConfirmPresenter

    var card: Card? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_leader_confirm)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (card == null) {
            card = gson.fromJson(args.getString("card"), Card::class.java)
        }

        Glide.with(activity!!)
                .load(Uri.parse("file:///android_asset/leader.png"))
                .into(view.leader_image)

        view.leader_name_text.text = card?.name
        view.leader_power_text.text = card?.description
        view.faction_name_text.text = activity!!.getStringResourceByName(Faction.ID_TO_KEY.apply(card?.faction))

        view.confirm_leader_button.setOnClickListener {
            MaterialDialog.Builder(activity!!)
                    .title("Confirm Deck Creation")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("Enter a name for your new deck", "New Deck 1", { dialog, input ->
                    })
                    .negativeText(android.R.string.cancel)
                    .positiveText(R.string.create_deck)
                    .onPositive { dialog, which ->
                        val deckName = dialog.inputEditText?.text.toString().trim()
                        presenter.saveNewDeck(deckName, card!!)
                    }
                    .show()
        }

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("card", gson.toJson(card))
        super.onSaveInstanceState(outState)
    }

    override fun onDeckSaved(deckId: Int) {
        router.setRoot(RouterTransaction.with(DeckbuildController(deckId))
                .pushChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.RIGHT))
                .popChangeHandler(FlipChangeHandler(FlipChangeHandler.FlipDirection.LEFT)))
        router.popToRoot()
    }

    override fun displayError(messageToDisplay: String) {
        Toast.makeText(activity, messageToDisplay, Toast.LENGTH_LONG).show()
    }

}
