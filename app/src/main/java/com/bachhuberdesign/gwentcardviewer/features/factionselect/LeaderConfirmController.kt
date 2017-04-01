package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import com.google.gson.Gson

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
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
        val view = container.inflate(R.layout.controller_faction_select)

        if (card == null) {
            card = Gson().fromJson(args.getString("card"), Card::class.java)
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("card", Gson().toJson(card))
        super.onSaveInstanceState(outState)
    }

//    override fun handleBack(): Boolean {
//        Log.d(TAG, "Handling back press")
//        router.popController(this)
//        return true
//    }


}
