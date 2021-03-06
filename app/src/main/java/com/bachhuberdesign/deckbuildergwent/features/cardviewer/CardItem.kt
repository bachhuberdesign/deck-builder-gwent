package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Lane
import com.bachhuberdesign.deckbuildergwent.util.getStringResourceByName
import com.bachhuberdesign.deckbuildergwent.util.gone
import com.bachhuberdesign.deckbuildergwent.util.invisible
import com.bachhuberdesign.deckbuildergwent.util.visible
import com.bumptech.glide.Glide
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.iconics.IconicsDrawable
import kotlinx.android.synthetic.main.item_card.view.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class CardItem(val card: Card, val isDeckbuildMode: Boolean) : AbstractItem<CardItem, CardItem.ViewHolder>() {

    companion object {
        @JvmStatic val TAG: String = CardItem::class.java.name
    }

    var count = 0

    override fun getLayoutRes(): Int {
        return R.layout.item_card
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun getType(): Int {
        return R.id.card_item
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.itemView.add_card_button.tag = "add"
        holder.itemView.remove_card_button.tag = "remove"

        Glide.clear(holder.cardImage)
        Glide.with(holder.itemView.context)
                .load(Uri.parse("file:///android_asset/cards/${card.iconUrl}"))
                .dontAnimate()
                .into(holder.cardImage)
        holder.cardImage.transitionName = "imageTransition${card.cardId}"

        holder.name.text = card.name
        holder.name.transitionName = "nameTransition${card.cardId}"

        holder.scrapCostText.text = card.scrap.toString()

        when (card.cardType) {
            CardType.BRONZE -> holder.bottomBar.setBackgroundResource(R.drawable.background_bronze_gradient)
            CardType.SILVER -> holder.bottomBar.setBackgroundResource(R.drawable.background_silver_gradient)
            CardType.GOLD -> holder.bottomBar.setBackgroundResource(R.drawable.background_gold_gradient)
            CardType.LEADER -> holder.bottomBar.setBackgroundResource(R.drawable.background_gold_gradient)
        }

        when (card.lane) {
            Lane.MELEE -> holder.laneIcon.setImageResource(R.drawable.lane_melee_icon)
            Lane.RANGED -> holder.laneIcon.setImageResource(R.drawable.lane_ranged_icon)
            Lane.SIEGE -> holder.laneIcon.setImageResource(R.drawable.lane_siege_icon)
            Lane.ALL -> holder.laneIcon.setImageResource(R.drawable.lane_melee_icon)
        }

        if (card.lane == Lane.EVENT) {
            holder.laneIcon.gone()
            holder.power.gone()
        } else {
            holder.laneIcon.visible()
            holder.power.visible()
        }

        holder.faction.text = holder.itemView.context.getStringResourceByName(Faction.ID_TO_KEY.apply(card.faction))
        holder.description.text = card.description
        holder.power.text = card.power.toString()

        if (isDeckbuildMode) {
            if (card.cardType == CardType.BRONZE) {
                holder.countText.text = "$count / 3"
            } else {
                holder.countText.text = "$count / 1"
            }

            holder.addCardButton.setImageDrawable(IconicsDrawable(holder.itemView.context)
                    .icon(CommunityMaterial.Icon.cmd_plus)
                    .color(Color.WHITE)
                    .sizeDp(12))

            holder.removeCardButton.setImageDrawable(IconicsDrawable(holder.itemView.context)
                    .icon(CommunityMaterial.Icon.cmd_minus)
                    .color(Color.WHITE)
                    .sizeDp(12))

            holder.addCardButton.visible()
            holder.removeCardButton.visible()

            if (card.cardType == CardType.BRONZE) {
                if (count >= 3) {
                    holder.addCardButton.invisible()
                }
            } else if (count >= 1) {
                holder.addCardButton.invisible()
            }

            if (count <= 0) {
                holder.removeCardButton.invisible()
            }
        } else {
            holder.countText.gone()
            // Not deckbuild mode, no need for add/remove buttons
            holder.addCardButton.invisible()
            holder.removeCardButton.invisible()
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        Glide.clear(holder.cardImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bottomBar: RelativeLayout = view.bottom_bar_relative_layout
        var name: TextView = view.card_name_text
        var faction: TextView = view.faction_name_text
        var description: TextView = view.card_description_text
        var power: TextView = view.card_power_text
        var cardImage: ImageView = view.card_image
        var countText: TextView = view.card_count_text
        var addCardButton: ImageButton = view.add_card_button
        var removeCardButton: ImageButton = view.remove_card_button
        var scrapCostText: TextView = view.scrap_cost_text
        var laneIcon: ImageView = view.lane_icon_image
    }

    class CardNameComparator(val isSortAscending: Boolean) : Comparator<CardItem> {
        override fun compare(lhs: CardItem, rhs: CardItem): Int {
            if (isSortAscending) {
                return lhs.card.name.compareTo(rhs.card.name)
            } else {
                return rhs.card.name.compareTo(lhs.card.name)
            }
        }
    }

    class CardTypeComparator(val isSortAscending: Boolean) : Comparator<CardItem> {
        override fun compare(lhs: CardItem, rhs: CardItem): Int {
            if (isSortAscending) {
                return rhs.card.cardType.compareTo(lhs.card.cardType)
            } else {
                return lhs.card.cardType.compareTo(rhs.card.cardType)
            }
        }
    }

    class CardRarityComparator(val isSortAscending: Boolean) : Comparator<CardItem> {
        override fun compare(lhs: CardItem, rhs: CardItem): Int {
            if (isSortAscending) {
                return lhs.card.rarity.compareTo(rhs.card.rarity)
            } else {
                return rhs.card.rarity.compareTo(lhs.card.rarity)
            }
        }
    }

    class CardScrapCostComparator(val isSortAscending: Boolean) : Comparator<CardItem> {
        override fun compare(lhs: CardItem, rhs: CardItem): Int {
            if (isSortAscending) {
                return lhs.card.scrap.compareTo(rhs.card.scrap)
            } else {
                return rhs.card.scrap.compareTo(lhs.card.scrap)
            }
        }
    }

    class CardFactionComparator(val isSortAscending: Boolean) : Comparator<CardItem> {
        override fun compare(lhs: CardItem, rhs: CardItem): Int {
            if (isSortAscending) {
                return lhs.card.faction.compareTo(rhs.card.faction)
            } else {
                return rhs.card.faction.compareTo(lhs.card.faction)
            }
        }
    }

    class CardLaneComparator(val isSortAscending: Boolean) : Comparator<CardItem> {
        override fun compare(lhs: CardItem, rhs: CardItem): Int {
            if (isSortAscending) {
                return lhs.card.lane.compareTo(rhs.card.lane)
            } else {
                return rhs.card.lane.compareTo(lhs.card.lane)
            }
        }
    }

}