package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.MainActivity
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.inject.module.ActivityModule
import com.bachhuberdesign.deckbuildergwent.util.changehandler.FabToDialogTransitionChangeHandler
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.controller_stat_track.view.*
import javax.inject.Inject

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class StatTrackController : Controller(), AddMatchDialogMvpContract {

    companion object {
        @JvmStatic val TAG: String = StatTrackController::class.java.name
    }

    @Inject
    lateinit var presenter: StatTrackPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_stat_track)

        (activity as MainActivity).persistedComponent
                .activitySubcomponent(ActivityModule(activity!!))
                .inject(this)

        activity!!.title = "Stat Tracker"

        view.fab.setOnClickListener {
            router.pushController(RouterTransaction.with(AddMatchDialogController())
                    .pushChangeHandler(FabToDialogTransitionChangeHandler())
                    .popChangeHandler(FabToDialogTransitionChangeHandler()))
        }

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