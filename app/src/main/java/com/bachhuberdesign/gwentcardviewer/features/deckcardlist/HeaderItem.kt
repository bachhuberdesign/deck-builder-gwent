package com.bachhuberdesign.gwentcardviewer.features.deckcardlist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bachhuberdesign.gwentcardviewer.R
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_header.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class HeaderItem : AbstractItem<HeaderItem, HeaderItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = HeaderItem::class.java.name
    }

    var leftText = ""
    var rightText = ""

    override fun getLayoutRes(): Int {
        return R.layout.item_header
    }

    override fun getType(): Int {
        return R.id.header_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.leftText.text = leftText
        holder.rightText.text = rightText
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var leftText: TextView = view.header_left_text
        var rightText: TextView = view.header_right_text
    }

}