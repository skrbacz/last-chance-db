package com.example.lastchancedb.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.databinding.FragmentQuestionScheduleAppointmentBinding


class QuestionSaveAppointmentDialogFragment : DialogFragment() {

    private var _binding: FragmentQuestionScheduleAppointmentBinding? = null
    private val binding get() = _binding!!
    private var submitBtn: Button? = null
    private var selectedDoseType: String? = null

    private var firstDoseBTN: RadioButton? = null
    private var nextDoseBTN: RadioButton? = null

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionScheduleAppointmentBinding.inflate(inflater, container, false)

        submitBtn = binding.submitBtn
        firstDoseBTN = binding.firstDoseBTN
        nextDoseBTN = binding.nextDoseBTN

        firstDoseBTN?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedDoseType = "First dose"
                submitBtn?.setOnClickListener {
                    showDialog()
                }
            }
        }

        nextDoseBTN?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedDoseType = "Next dose"
                submitBtn?.setOnClickListener {
                    showDialog()
                }
            }

        }

        return binding.root
    }

    fun showDialog() {
        val showDialog = selectedDoseType?.let { SaveAppointmentDialogFragment(it) }
        showDialog?.show((context as AppCompatActivity).supportFragmentManager, "dialog")
        dismiss()
    }

}