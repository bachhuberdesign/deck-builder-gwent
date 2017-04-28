package com.bachhuberdesign.deckbuildergwent.features.factionselect

import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Card
import com.bachhuberdesign.deckbuildergwent.features.shared.model.Faction
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class FactionSelectPresenter
@Inject constructor(private val repository: DeckRepository) : BasePresenter<FactionSelectMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = FactionSelectPresenter::class.java.name
    }

    override fun attach(view: FactionSelectMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    /**
     *
     */
    fun loadFactions() {
        val factions: MutableList<Faction> = ArrayList()
        val factionsCursor = repository.getFactions()
        val leadersCursor = repository.getLeaders()

        factionsCursor.use {
            while (factionsCursor.moveToNext()) {
                factions.add(Faction.MAPPER.apply(factionsCursor))
            }
        }

        leadersCursor.use {
            while (leadersCursor.moveToNext()) {
                val card = Card.MAPPER.apply(leadersCursor)
                factions.forEachIndexed { index, faction ->
                    if (card.faction == faction.id) {
                        factions[index].leaders.add(card)
                    }
                }
            }
        }

        if (isViewAttached()) {
            view!!.onFactionsLoaded(factions)
        }

    }


}