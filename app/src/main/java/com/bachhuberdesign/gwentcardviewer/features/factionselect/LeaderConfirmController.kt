package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.controller_leader_confirm.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
// TODO: Inject presenter and Gson
class LeaderConfirmController : Controller {

    var card: Card? = null

    constructor(card: Card) : super() {
        this.card = card
    }

    constructor(args: Bundle) : super()

    companion object {
        val TAG: String = this::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_leader_confirm)

        if (card == null) {
            card = Gson().fromJson(args.getString("card"), Card::class.java)
        }

        Glide.with(activity)
                .load(Uri.parse("file:///android_asset/leader.png"))
                .into(view.leader_image)

        view.leader_name_text.text = card?.name
        view.leader_power_text.text = card?.description
        view.faction_name_text.text = activity!!.getStringResourceByName(Faction.ID_TO_KEY.apply(card?.faction))

        Log.d(TAG, "Power: ${card?.description}")

        view.confirm_leader_button.setOnClickListener {

            MaterialDialog.Builder(activity!!)
                    .title("Confirm Deck Creation")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("Enter a name for your new deck", "New Deck 1", { dialog, input ->
                    })
                    .negativeText(android.R.string.cancel)
                    .positiveText(R.string.create_deck)
                    .onPositive { dialog, which ->
                        Toast.makeText(activity, "Persist new deck and pass id to DeckbuildController here", Toast.LENGTH_LONG).show()
                    }
                    .show()
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("card", Gson().toJson(card))
        super.onSaveInstanceState(outState)
    }

}
