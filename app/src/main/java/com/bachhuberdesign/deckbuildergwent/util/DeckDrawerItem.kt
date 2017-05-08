package com.bachhuberdesign.deckbuildergwent.util

import android.net.Uri
import android.support.annotation.ColorRes
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bachhuberdesign.deckbuildergwent.R
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

    var deckName: String = ""
    var deckId: Int = 0
    var backgroundUrl: String = ""
    var backgroundColor: Int = 0

    fun withDeckName(deckName: String): DeckDrawerItem {
        this.deckName = deckName
        return this
    }

    fun withDeckId(deckId: Int): DeckDrawerItem {
        this.deckId = deckId
        return this
    }

    fun withBackgroundUrl(backgroundUrl: String): DeckDrawerItem {
        this.backgroundUrl = backgroundUrl
        return this
    }

    fun withBackgroundColor(@ColorRes backgroundColor: Int): DeckDrawerItem {
        this.backgroundColor = backgroundColor
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

        if (backgroundColor > 0) {
            holder.constraintLayout.setBackgroundResource(backgroundColor)
        }

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
        var constraintLayout: ConstraintLayout = view.constraint_layout
    }

}