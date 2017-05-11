package com.bachhuberdesign.deckbuildergwent.inject

import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardDetailController
import com.bachhuberdesign.deckbuildergwent.features.cardviewer.CardViewerController
import com.bachhuberdesign.deckbuildergwent.features.deckbuild.DeckbuildController
import com.bachhuberdesign.deckbuildergwent.features.deckdetail.DeckDetailController
import com.bachhuberdesign.deckbuildergwent.features.deckselect.DeckSelectController
import com.bachhuberdesign.deckbuildergwent.features.factionselect.FactionSelectController
import com.bachhuberdesign.deckbuildergwent.features.factionselect.LeaderConfirmController
import com.bachhuberdesign.deckbuildergwent.features.shared.base.BaseActivity
import com.bachhuberdesign.deckbuildergwent.inject.annotation.ActivityScope
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import dagger.Subcomponent

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(baseActivity: BaseActivity)

    fun inject(deckbuildController: DeckbuildController)

    fun inject(factionSelectController: FactionSelectController)

    fun inject(leaderConfirmController: LeaderConfirmController)

    fun inject(cardViewerController: CardViewerController)

    fun inject(deckSelectController: DeckSelectController)

    fun inject(deckDetailController: DeckDetailController)

    fun inject(cardDetailController: CardDetailController)

}
