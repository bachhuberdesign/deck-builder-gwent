package com.bachhuberdesign.deckbuildergwent.features.cardviewer

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.CardType
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.util.getStringResourceByName
import com.bachhuberdesign.deckbuildergwent.util.invisible
import com.bachhuberdesign.deckbuildergwent.util.visible
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.items.AbstractItem
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

        Glide.clear(holder.cardImage)
        Glide.with(holder.itemView.context)
                .load(Uri.parse("file:///android_asset/cards/${card.iconUrl}"))
                .into(holder.cardImage)

        holder.name.text = card.name
        holder.faction.text = holder.itemView.context.getStringResourceByName(Faction.ID_TO_KEY.apply(card.faction))
        holder.description.text = card.description
        holder.power.text = card.power.toString()

        if (isDeckbuildMode) {
            if (card.cardType == CardType.BRONZE) {
                holder.countText.text = "$count / 3"
            } else {
                holder.countText.text = "$count / 1"
            }

            holder.addCardButton.visible()
        } else {
            holder.addCardButton.invisible()
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        Glide.clear(holder.cardImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.card_name_text
        var faction: TextView = view.faction_name_text
        var description: TextView = view.card_description_text
        var power: TextView = view.card_power_text
        var cardImage: ImageView = view.card_image
        var countText: TextView = view.card_count_text
        var addCardButton: Button = view.add_card_button
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