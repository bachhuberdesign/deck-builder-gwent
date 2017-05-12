package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.controller_card_detail.view.*
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
        activity?.title = card.name

        Glide.with(activity!!)
                .load(Uri.parse("file:///android_asset/cards/${card.iconUrl}"))
                .fitCenter()
                .into(view!!.card_image)

        view!!.card_name_text.text = card.name
        view!!.card_description_text.text = card.description
        view!!.card_flavor_text.text = card.flavorText
        view!!.card_group_text.text = card.cardType.toString()
        view!!.card_faction_text.text = card.faction.toString()
        view!!.card_rarity_text.text = card.rarity.toString()
        view!!.card_lane_text.text = card.lane.toString()
        view!!.card_craft_text.text = "${card.scrap} / ${card.scrapPremium}"
        view!!.card_mill_text.text = "${card.mill} / ${card.millPremium}"
    }

}