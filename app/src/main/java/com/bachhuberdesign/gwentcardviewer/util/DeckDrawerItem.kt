package com.bachhuberdesign.gwentcardviewer.util

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bachhuberdesign.gwentcardviewer.R
import com.bumptech.glide.Glide
import com.mikepenz.materialdrawer.model.AbstractDrawerItem
import kotlinx.android.synthetic.main.item_deck_drawer.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class DeckDrawerItem : AbstractDrawerItem<DeckDrawerItem, DeckDrawerItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = DeckDrawerItem::class.java.name
    }

    private var deckName: String = ""
    private var backgroundUrl: String = ""

    fun withDeckName(deckName: String): DeckDrawerItem {
        this.deckName = deckName
        return this
    }

    fun withBackgroundUrl(backgroundUrl: String): DeckDrawerItem {
        this.backgroundUrl = backgroundUrl
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_deck_drawer
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun getType(): Int {
        return R.id.deck_drawer_item
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.nameText.text = deckName

        if (backgroundUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                    .load(Uri.parse(backgroundUrl))
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.background)
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.card_name_text
        var background: ImageView = view.slim_card_background
    }

}