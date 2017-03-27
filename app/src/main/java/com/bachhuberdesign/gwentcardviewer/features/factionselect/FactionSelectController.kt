package com.bachhuberdesign.gwentcardviewer.features.factionselect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.gwentcardviewer.R
import com.bachhuberdesign.gwentcardviewer.features.deckbuild.DeckbuildActivity
import com.bachhuberdesign.gwentcardviewer.inject.module.ActivityModule
import com.bachhuberdesign.gwentcardviewer.util.inflate
import com.bluelinelabs.conductor.Controller
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class FactionSelectController : Controller() {

    companion object {
        @JvmStatic val TAG: String = this::class.java.name
    }

    @Inject
    lateinit var presenter: FactionSelectPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_deckbuild)

        val persistedComponent = (activity as DeckbuildActivity)
                .persistedComponent.activitySubcomponent(ActivityModule(activity as DeckbuildActivity))
        persistedComponent.inject(this)

        return view
    }

}