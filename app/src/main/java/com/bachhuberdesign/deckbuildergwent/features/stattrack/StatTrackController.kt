package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class StatTrackController : Controller(), StatTrackMvpContract {

    companion object {
        @JvmStatic val TAG: String = StatTrackController::class.java.name
    }

    @Inject
    lateinit var presenter: StatTrackPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_stat_track)

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attach(this)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.detach()
    }

}