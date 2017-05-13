package com.bachhuberdesign.deckbuildergwent.features.stattrack

import android.app.DatePickerDialog
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachhuberdesign.deckbuildergwent.R
import com.bachhuberdesign.deckbuildergwent.util.inflate
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_add_match_dialog.view.*
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

                    getView()?.date_text?.text = DateFormat.format("MMMM dd, yyyy", calendar.time)
                }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        view.date_text.setOnClickListener {
            datePickerDialog.show()
        }

        view.date_text.text = DateFormat.format("MMMM dd, yyyy", Calendar.getInstance().time)

        return view
    }


    override fun onAttach(view: View) {
        super.onAttach(view)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
    }

}