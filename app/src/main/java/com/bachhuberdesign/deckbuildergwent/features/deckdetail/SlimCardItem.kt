package com.bachhuberdesign.deckbuildergwent.features.deckdetail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.ISwipeable
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_slim_card.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class SlimCardItem : AbstractItem<SlimCardItem, SlimCardItem.ViewHolder>(), ISwipeable<SlimCardItem, IItem<*, *>> {

    companion object {
        @JvmStatic val TAG: String = SubHeaderItem::class.java.name
        @JvmStatic val TYPE: Int = R.id.slim_card_item
    }

    private var swipeable: Boolean = true
    var card: Card? = null
    var count: Int = 0

    override fun isSwipeable(): Boolean {
        return swipeable
    }

    override fun withIsSwipeable(swipeable: Boolean): SlimCardItem {
        this.swipeable = swipeable
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_slim_card
    }

    override fun getType(): Int {
        return R.id.slim_card_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.nameText.text = card?.name

        if (count > 0) {
            holder.countText.text = count.toString()
        } else {
            holder.countText.text = ""
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.card_name_text
        var countText: TextView = view.card_count_text
    }

}