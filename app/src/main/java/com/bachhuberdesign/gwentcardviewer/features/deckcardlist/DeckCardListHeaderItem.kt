package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bachhuberdesign.gwentcardviewer.R
import com.mikepenz.fastadapter.commons.items.AbstractExpandableItem

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckCardListHeaderItem : AbstractExpandableItem<DeckCardListHeaderItem, DeckCardListHeaderItem.ViewHolder, DeckCardListItem>() {

    override fun getLayoutRes(): Int {
        return R.layout.item_deck_card_header
    }

    override fun getType(): Int {
        return R.id.deck_card_list_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    companion object {
        @JvmStatic val TAG: String = DeckCardListHeaderItem::class.java.name
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

}