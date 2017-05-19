package com.bachhuberdesign.deckbuildergwent.features.deckselect

import android.net.Uri
import android.support.annotation.ColorRes
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bachhuberdesign.deckbuildergwent.R
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_leader.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class LeaderItem : AbstractItem<LeaderItem, LeaderItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = LeaderItem::class.java.name
    }

    var leaderName: String = ""
    var backgroundUrl: String = ""
    var backgroundColor: Int = 0

    fun withLeaderName(leaderName: String): LeaderItem {
        this.leaderName = leaderName
        return this
    }

    fun withBackgroundUrl(backgroundUrl: String): LeaderItem {
        this.backgroundUrl = backgroundUrl
        return this
    }

    fun withBackgroundColor(@ColorRes backgroundColor: Int): LeaderItem {
        this.backgroundColor = backgroundColor
        return this
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_leader
    }

    override fun getType(): Int {
        return R.id.leader_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.nameText.text = leaderName

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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = view.card_name_text
        var background: ImageView = view.slim_card_background
        var constraintLayout: ConstraintLayout = view.constraint_layout
    }

}