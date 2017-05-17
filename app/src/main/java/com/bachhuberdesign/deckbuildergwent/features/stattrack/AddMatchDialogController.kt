package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.app.DatePickerDialog
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_add_match_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
class AddMatchDialogController : Controller(), AddMatchDialogMvpContract {

    companion object {
        @JvmStatic val TAG: String = AddMatchDialogController::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_add_match_dialog)

        val datePickerDialog = DatePickerDialog(activity!!,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    getView()?.date_played_text?.text = DateFormat.format("MMMM dd, yyyy", calendar.time)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        view.date_played_text.setOnClickListener {
            datePickerDialog.show()
        }

        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)

        view.date_played_text.text = formatter.format(Calendar.getInstance().time)

        view.add_match_button.setOnClickListener {
            val controller: StatTrackController = router.getControllerWithTag(StatTrackController.TAG) as StatTrackController

            val match: Match = Match()

            val radioButtonID = view.outcome_radio_group.checkedRadioButtonId
            val radioButton = view.outcome_radio_group.findViewById(radioButtonID)
            val outcomeIndex = view.outcome_radio_group.indexOfChild(radioButton)

            if (outcomeIndex == -1) {
                Toast.makeText(activity!!, "Match outcome must be selected.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            match.outcome = outcomeIndex + 1
            match.notes = view.notes_edit_text.text.trim().toString()
            match.playedDate = formatter.parse(view.date_played_text.text.toString())

            controller.addMatch(match)
        }

        return view
    }

}