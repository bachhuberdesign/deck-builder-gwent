package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bachhuberdesign.gwentcardviewer.R
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.utils.ViewHolderFactory
import kotlinx.android.synthetic.main.faction_select_item.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class FactionSelectItem : AbstractItem<FactionSelectItem, FactionSelectItem.ViewHolder>() {

    private val FACTORY = ItemFactory()

    var backgroundUrl = ""
    var factionName = ""
    var factionDescription = ""

    override fun getLayoutRes(): Int {
        return R.layout.faction_select_item
    }

    override fun getType(): Int {
        return 777
    }

    override fun bindView(viewHolder: ViewHolder, payloads: List<Any>?) {
        super.bindView(viewHolder, payloads)

        viewHolder.name.text = factionName
        viewHolder.description.text = factionDescription

        Glide.clear(viewHolder.image)

        Glide.with(viewHolder.itemView.context)
                .load(backgroundUrl)
                .into(viewHolder.image)
    }

    override fun unbindView(viewHolder: ViewHolder) {
        super.unbindView(viewHolder)

        viewHolder.name.text = null
        viewHolder.description.text = null

        Glide.clear(viewHolder.image)
        viewHolder.image.setImageDrawable(null)
    }

    private class ItemFactory : ViewHolderFactory<ViewHolder> {
        override fun create(v: View): ViewHolder {
            return ViewHolder(v)
        }
    }

    override fun getFactory(): ViewHolderFactory<out ViewHolder> {
        return FACTORY
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.faction_name_text
        var description: TextView = view.faction_description_text
        var image: ImageView = view.faction_image
    }

}