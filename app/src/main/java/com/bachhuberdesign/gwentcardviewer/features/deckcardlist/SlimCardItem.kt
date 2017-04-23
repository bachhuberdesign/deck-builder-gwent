package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bachhuberdesign.gwentcardviewer.R
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.ISwipeable
import com.mikepenz.fastadapter.items.AbstractItem

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class SlimCardItem : AbstractItem<SlimCardItem, SlimCardItem.ViewHolder>(), ISwipeable<SlimCardItem, IItem<*, *>> {

    companion object {
        @JvmStatic val TAG: String = SubHeaderItem::class.java.name
    }

    private var swipeable: Boolean = true

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
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

}