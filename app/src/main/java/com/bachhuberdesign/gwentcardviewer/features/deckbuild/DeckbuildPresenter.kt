package com.bachhuberdesign.gwentcardviewer.features.deckbuild

import com.bachhuberdesign.gwentcardviewer.features.shared.base.BasePresenter
import com.bachhuberdesign.gwentcardviewer.inject.annotation.PersistedScope
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class DeckbuildPresenter
@Inject constructor(private val repository: DeckRepository) : BasePresenter<DeckbuildMvpContract>() {

    override fun attach(view: DeckbuildMvpContract) {
        super.attach(view)
    }

    override fun detach() {
        super.detach()
    }

    fun loadUserDecks() {
        val decks: MutableList<Deck> = ArrayList()
        val query = repository.queryUserCreatedDecks()

        query.subscribe({ query ->
            val cursor = query.run()

            query.run().use {
                while (cursor!!.moveToNext()) {
                    decks.add(Deck.MAPPER.apply(cursor))
                }
            }

            if (decks.isNotEmpty() && isViewAttached()) {
                view!!.onDecksLoaded(decks)
            }
        })

    }

}