package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.gwentcardviewer.MainActivity
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class CardViewerController : Controller, CardViewerMvpContract {

    constructor(factionId: Int) : super() {
        this.factionId = factionId
    }

    constructor(args: Bundle) : super()

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    @Inject
    lateinit var presenter: CardViewerPresenter

    var factionId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_cardviewer)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        if (factionId == 0) {
            factionId = args.getInt("factionId")
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
        presenter.getUsableCards(factionId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("factionId", factionId)
        super.onSaveInstanceState(outState)
    }

    override fun onCardsLoaded(cards: List<Card>) {
        TODO("Not yet implemented.")
    }

}