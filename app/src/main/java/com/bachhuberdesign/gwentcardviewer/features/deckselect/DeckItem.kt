package com.bachhuberdesign.gwentcardviewer.features.deckselect

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.shared.model.Faction
import com.bachhuberdesign.gwentcardviewer.util.getStringResourceByName
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_deck.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckItem : AbstractItem<DeckItem, DeckItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = DeckItem::class.java.name
    }

    var deckName = ""
    var factionId: Int = 0
    var leaderName = ""
    var deckId: Int = 0

    override fun getLayoutRes(): Int {
        return R.layout.item_deck
    }

    override fun getType(): Int {
        return R.id.deck_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.deckName.text = deckName
        holder.factionName.text = holder.itemView.context.getStringResourceByName(Faction.ID_TO_KEY.apply(factionId))
        holder.leaderName.text = leaderName
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        holder.deckName.text = null
        holder.factionName.text = null
        holder.leaderName.text = null
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var factionName: TextView = view.faction_name_text
        var deckName: TextView = view.deck_name_text
        var leaderName: TextView = view.leader_name_text
    }

}