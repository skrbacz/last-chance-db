package com.example.lastchancedb.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.database.vaccination_record.VaccinationRecordSuspendedFunctions
import com.example.lastchancedb.databinding.FragmentUpdateVaccRecDialogBinding
import com.example.lastchancedb.databinding.FragmentUpdateVaccinationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
/**
 * A dialog fragment for updating vaccination records.
 * This fragment allows users to update vaccination records such as setting the administration date
 * of the first dose, the date for the next dose, and toggling between doses.
 */
class UpdateVaccRecDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var userEmail = Firebase.auth.currentUser?.email.toString()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var vaccinationRecords: Set<VaccinationRecord?>? = null

    private var _binding: FragmentUpdateVaccRecDialogBinding? = null
    private val binding get() = _binding!!

    private var checkBox: CheckBox? = null
    private var firstDoseTV: TextView? = null
    private var secondDoseTV: TextView? = null
    private var updateBtn: Button? = null

    private var secondDate: Date? = null
    private var firstDate: Date? = null
    private var selectedDate: Date? = null
    private var minDateInSecondDose: Date? = null

    private var selectedDateFlag: Int = 0

    /**
     * Called when the fragment is resumed. This method sets the dialog window dimensions,
     * retrieves the list of vaccination records, and initializes the dropdown list of vaccination names.
     */
    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params

        coroutineScope.launch {
            vaccinationRecords =
                VaccinationRecordSuspendedFunctions.getAllVaccRecByUserEmail(userEmail)

            binding.apply {
                val vaccNames = getVaccNamesFromVaccRec(vaccinationRecords)
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.drop_down_item, vaccNames)
                vaccinationNameUpdateACTV.setAdapter(arrayAdapter)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateVaccRecDialogBinding.inflate(inflater, container, false)
        checkBox = binding.checkBoxUpdate
        firstDoseTV = binding.firstDDTV
        secondDoseTV = binding.secondDDTV
        updateBtn = binding.updateVRBTN

        checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                secondDoseTV?.visibility = View.VISIBLE
                updateBtn?.setOnClickListener {
                    if (binding.vaccinationNameUpdateACTV.text.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Please choose a vaccination",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (firstDate == null) {
                        Toast.makeText(
                            requireContext(),
                            "Please choose a first dose date",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (selectedDate == null) {
                        Toast.makeText(
                            requireContext(),
                            "Please choose a second dose date",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        coroutineScope.launch {
                            val vaccRec =
                                VaccinationRecordSuspendedFunctions.getVaccRecByUserEmailVaccName(
                                    userEmail,
                                    binding.vaccinationNameUpdateACTV.text.toString()
                                )
                            vaccRec?.dateAdministrated = firstDate
                            vaccRec?.nextDoseDate = secondDate

                            if (vaccRec != null) {
                                vaccRec.id?.let { it1 ->
                                    VaccinationRecordSuspendedFunctions.updateVaccRec(
                                        it1, vaccRec, requireContext()
                                    )
                                }
                            }
                        }

                    }
                }
            } else {
                secondDoseTV?.visibility = View.GONE
            }
        }
        updateBtn?.setOnClickListener {
            if (binding.vaccinationNameUpdateACTV.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please choose a vaccination",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (firstDate == null) {
                Toast.makeText(
                    requireContext(),
                    "Please choose a first dose date",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                coroutineScope.launch {
                    val vaccRec =
                        VaccinationRecordSuspendedFunctions.getVaccRecByUserEmailVaccName(
                            userEmail,
                            binding.vaccinationNameUpdateACTV.text.toString()
                        )
                    vaccRec?.dateAdministrated = firstDate
                    vaccRec?.nextDoseDate = null

                    if (vaccRec != null) {
                        vaccRec.id?.let { it1 ->
                            VaccinationRecordSuspendedFunctions.updateVaccRec(
                                it1, vaccRec, requireContext()
                            )
                        }
                    }
                }
            }
        }

        firstDoseTV?.setOnClickListener {
            selectedDateFlag = 1
            openDialog()
        }

        secondDoseTV?.setOnClickListener {
            selectedDateFlag = 2
            openDialog()
        }
        return binding.root
    }
    /**
     * Opens a date picker dialog to select a date.
     */
    private fun openDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(
            requireContext(),
            R.style.CustomCalendarDialogTheme,
            this,
            year,
            month,
            day,
        )

        if(selectedDateFlag == 1){
            dialog.datePicker.maxDate = System.currentTimeMillis()
        }else if(selectedDateFlag == 2){
            dialog.datePicker.minDate = minDateInSecondDose?.time ?: System.currentTimeMillis()
        }

        dialog.datePicker.firstDayOfWeek = Calendar.MONDAY
        dialog.show()
    }
    /**
     * Callback method to be invoked when the user sets a date in the date picker dialog.
     */
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        selectedDate = java.sql.Date(calendar.timeInMillis)
        val formattedDate = formatDate(selectedDate!!)
        if (selectedDateFlag == 1) {
            firstDoseTV?.text = formattedDate
            minDateInSecondDose = selectedDate
            firstDate = selectedDate
        } else if (selectedDateFlag == 2) {
            secondDoseTV?.text = formattedDate
            secondDate = selectedDate
        }
    }
    /**
     * Formats the given date to a string in the format "yyyy-MM-dd".
     */
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * Retrieves the vaccination names from the given list of vaccination records.
     */
    private fun getVaccNamesFromVaccRec(vaccRecs: Set<VaccinationRecord?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccRecs?.forEach { vaccRec ->
            vaccNames.add(vaccRec?.vaccName.toString())
        }
        return vaccNames.toTypedArray()
    }
}