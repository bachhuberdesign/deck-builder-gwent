package com.bachhuberdesign.gwentcardviewer.features.cardviewer

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.card_item.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class CardItem : AbstractItem<CardItem, CardItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    var card: Card? = null

    override fun getLayoutRes(): Int {
        return R.layout.card_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun getType(): Int {
        return 888
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        Glide.clear(holder.cardImage)
        Glide.with(holder.itemView.context)
                .load(card?.iconUrl)
                .into(holder.cardImage)

        holder.name.text = card?.name
        holder.faction.text = holder.itemView.context.getStringResourceByName(Faction.ID_TO_KEY.apply(card?.faction))
        holder.description.text = card?.description
        holder.power.text = card?.power.toString()
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        Glide.clear(holder.cardImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.card_name_text
        var faction: TextView = view.faction_name_text
        var description: TextView = view.card_description_text
        var power: TextView = view.card_power_text
        var cardImage: ImageView = view.card_image
    }

}