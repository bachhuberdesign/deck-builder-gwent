package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Card
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.faction_select_item.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class FactionSelectItem : AbstractItem<FactionSelectItem, FactionSelectItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = FactionSelectItem::class.java.name
    }

    var backgroundUrl = ""
    var factionName = ""
    var factionDescription = ""
    var leaders: List<Card>? = null

    override fun getLayoutRes(): Int {
        return R.layout.faction_select_item
    }

    override fun getType(): Int {
        return 777
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun bindView(viewHolder: ViewHolder, payloads: List<Any>?) {
        super.bindView(viewHolder, payloads)
        viewHolder.cardView.clipToOutline = false

        viewHolder.name.text = factionName
        viewHolder.description.text = factionDescription

        Glide.clear(viewHolder.leader1)
        Glide.clear(viewHolder.leader2)
        Glide.clear(viewHolder.leader3)

        Glide.with(viewHolder.itemView.context)
                .load(leaders?.get(0)?.iconUrl)
                .into(viewHolder.leader1)

        Glide.with(viewHolder.itemView.context)
                .load(leaders?.get(1)?.iconUrl)
                .fitCenter()
                .into(viewHolder.leader2)

        Glide.with(viewHolder.itemView.context)
                .load(leaders?.get(2)?.iconUrl)
                .fitCenter()
                .into(viewHolder.leader3)
    }

    override fun unbindView(viewHolder: ViewHolder) {
        super.unbindView(viewHolder)

        viewHolder.name.text = null
        viewHolder.description.text = null

        Glide.clear(viewHolder.leader1)
        Glide.clear(viewHolder.leader2)
        Glide.clear(viewHolder.leader3)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: CardView = view.card_view
        var name: TextView = view.faction_name_text
        var description: TextView = view.faction_description_text

        var leader1: ImageView = view.leader1_image
        var leader2: ImageView = view.leader2_image
        var leader3: ImageView = view.leader3_image
    }

}