package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bachhuberdesign.gwentcardviewer.R
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_sub_header.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class SubHeaderItem : AbstractItem<SubHeaderItem, SubHeaderItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = SubHeaderItem::class.java.name
    }

    var textLeft = ""
    var textRight = ""

    override fun getLayoutRes(): Int {
        return R.layout.item_sub_header
    }

    override fun getType(): Int {
        return R.id.sub_header_item
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
        var leftText: TextView = view.sub_header_left_text
        var rightText: TextView = view.sub_header_right_text
    }

}