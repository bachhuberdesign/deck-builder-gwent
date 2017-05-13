package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class AddMatchDialogController : Controller(), AddMatchDialogMvpContract {

    companion object {
        @JvmStatic val TAG: String = AddMatchDialogController::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_add_match_dialog)

        return view
    }


    override fun onAttach(view: View) {
        super.onAttach(view)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
    }

}