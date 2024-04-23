package com.example.lastchancedb.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.lastchancedb.R
import com.google.android.material.button.MaterialButtonToggleGroup


class QuestionScheduleAppointmentFragment : DialogFragment() {

    private lateinit var doseToggleGroup: MaterialButtonToggleGroup
    private var submitBtn: Button? = null
    private var selectedDoseType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question_schedule_appointment, container, false)

        submitBtn= view.findViewById(R.id.submitBtn)
        doseToggleGroup = view.findViewById(R.id.toggleButtonGroup)

        submitBtn?.setOnClickListener {
            if (getSelectedDoseType() == "default") {
                Toast.makeText(requireContext(), "Please choose a dose type", Toast.LENGTH_SHORT).show()
            } else {
                val showPopUp = selectedDoseType?.let { it1 -> ScheduleAppointmentFragment(it1) }
                showPopUp?.show((activity as AppCompatActivity).supportFragmentManager,"showScheduleAppointmentFragment")            }
        }

        doseToggleGroup.addOnButtonCheckedListener{ group, checkedId, isChecked ->

            if (isChecked) {
                selectedDoseType = getSelectedDoseType()
            }else{
                selectedDoseType = "default"
            }

        }

        return view
    }

    fun getSelectedDoseType(): String {
        val checkedButtonId= doseToggleGroup.checkedButtonId
        return if (checkedButtonId != View.NO_ID) {
            val checkedButton= doseToggleGroup.findViewById<Button>(checkedButtonId)
            checkedButton.text.toString()
        } else {
            "default"
        }
    }

}