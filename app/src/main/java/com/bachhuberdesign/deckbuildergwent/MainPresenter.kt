package com.bachhuberdesign.deckbuildergwent

import android.util.Log
import com.bachhuberdesign.MainMvpContract
import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardRepository
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckRepository
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BasePresenter
import com.bachhuberdesign.deckbuildergwent.inject.annotation.PersistedScope
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@PersistedScope
class MainPresenter
@Inject constructor(val deckRepository: DeckRepository,
                    val cardRepository: CardRepository) : BasePresenter<MainMvpContract>() {

    companion object {
        @JvmStatic val TAG: String = MainPresenter::class.java.name
    }

    var deckSubscription: Subscription? = null

    override fun attach(view: MainMvpContract) {
        super.attach(view)

        deckSubscription = deckRepository.observeRecentlyUpdatedDecks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ decks ->

                    decks.forEach { deck ->
                        deck.leader = cardRepository.getCardById(deck.leaderId)
                    }
                    if (decks.isNotEmpty() && isViewAttached()) {
                        view.showRecentDecksInDrawer(decks)
                    }
                }, { error ->
                    Log.e(TAG, "Error querying decks.", error)
                })
    }

    override fun detach() {
        super.detach()

        deckSubscription?.unsubscribe()
    }

}