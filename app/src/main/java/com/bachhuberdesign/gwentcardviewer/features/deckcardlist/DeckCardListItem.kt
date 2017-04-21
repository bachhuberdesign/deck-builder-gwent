package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bachhuberdesign.gwentcardviewer.R
import com.mikepenz.fastadapter.items.AbstractItem

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckCardListItem : AbstractItem<DeckCardListItem, DeckCardListItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = DeckCardListItem::class.java.name
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_deck_card
    }

    override fun getType(): Int {
        return R.id.deck_card_list_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

}