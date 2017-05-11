package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class CardDetailController : Controller, CardDetailMvpContract {

    constructor(cardId: Int) {
        this.cardId = cardId
    }

    constructor(args: Bundle) : super()

    companion object {
        @JvmStatic val TAG: String = CardDetailController::class.java.name
    }

    @Inject
    lateinit var presenter: CardDetailPresenter

    var cardId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_card_detail)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
        presenter.loadCard(cardId)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // TODO: Save cardId here

        super.onSaveInstanceState(outState)
    }

    override fun onCardLoaded(card: Card) {
        Log.d(TAG, "onCardLoaded(): $card")

        // TODO: Set views
    }

}