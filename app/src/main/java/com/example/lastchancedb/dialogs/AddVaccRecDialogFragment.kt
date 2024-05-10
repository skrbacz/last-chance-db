package com.example.lastchancedb.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.database.vaccination.VaccinationSuspendedFunctions
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.database.vaccination_record.VaccinationRecordSuspendedFunctions
import com.example.lastchancedb.databinding.FragmentAddVaccinationRecordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.sql.Date
import java.util.Calendar
import java.util.Locale


/**
 * Dialog fragment for adding vaccination records.
 *
 * This dialog allows users to add vaccination records including the date of the first dose and optionally the date of the second dose.
 */
class AddVaccRecDialogFragment() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var userEmail = Firebase.auth.currentUser?.email.toString()

    private var _binding: FragmentAddVaccinationRecordBinding? = null
    private val binding get() = _binding!!

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var vaccinations: Set<Vaccination?>? = null
    private var addBtn: Button? = null
    private var firstDoseDateTV: TextView? = null
    private var secondDoseDateTV: TextView? = null
    private var checkBox: CheckBox? = null

    private var secondDate: Date? = null
    private var firstDate: Date? = null

    private var selectedDate: Date? = null
    private var minDateInSecondDose: Date? = null

    private var selectedDateFlag: Int = 0

    /**
     * Called when the fragment is resumed. This method sets the dialog window dimensions
     * and initializes the dropdown list based on the type of dose.
     */
    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params

        coroutineScope.launch {
            vaccinations = VaccinationSuspendedFunctions.getAllVaccs()

            binding.apply {
                val vaccNames = getVaccNames(vaccinations)
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.drop_down_item, vaccNames)
                vaccinationNameInsertACTV.setAdapter(arrayAdapter)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddVaccinationRecordBinding.inflate(inflater, container, false)

        addBtn = binding.addVaccRecBTN
        firstDoseDateTV = binding.firstDoseDateTV

        checkBox = binding.checkBox
        secondDoseDateTV = binding.secondDoseDateTV

        checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                secondDoseDateTV?.visibility = View.VISIBLE

                addBtn?.setOnClickListener {
                    if (binding.vaccinationNameInsertACTV.text.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Please choose a vaccination",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (firstDate == null) {
                        Toast.makeText(requireContext(), "Please choose a date", Toast.LENGTH_SHORT)
                            .show()
                    } else if (selectedDate == null) {
                        Toast.makeText(
                            requireContext(),
                            "Please choose a date of second dose",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        coroutineScope.launch {
                            val vaccRec = firstDate?.let { it1 ->
                                VaccinationRecord(
                                    null,
                                    userEmail,
                                    binding.vaccinationNameInsertACTV.text.toString(),
                                    it1,
                                    secondDate
                                )
                            }
                            VaccinationRecordSuspendedFunctions.insertVaccRec(
                                vaccRec!!,
                                requireContext()
                            )
                        }
                    }

                }
            } else {
                secondDoseDateTV?.visibility = View.GONE
                addBtn?.setOnClickListener {
                    if (binding.vaccinationNameInsertACTV.text.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Please choose a vaccination",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (firstDate == null) {
                        Toast.makeText(requireContext(), "Please choose a date", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        coroutineScope.launch {
                            val vaccRec = firstDate?.let { it1 ->
                                VaccinationRecord(
                                    null,
                                    userEmail,
                                    binding.vaccinationNameInsertACTV.text.toString(),
                                    it1,
                                    null
                                )
                            }
                            VaccinationRecordSuspendedFunctions.insertVaccRec(
                                vaccRec!!,
                                requireContext()
                            )
                        }
                    }

                }
            }
        }

        addBtn?.setOnClickListener {
            if (binding.vaccinationNameInsertACTV.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please choose a vaccination",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (firstDate == null) {
                Toast.makeText(requireContext(), "Please choose a date", Toast.LENGTH_SHORT)
                    .show()
            } else {
                coroutineScope.launch {
                    val vaccRec = firstDate?.let { it1 ->
                        VaccinationRecord(
                            null,
                            userEmail,
                            binding.vaccinationNameInsertACTV.text.toString(),
                            it1,
                            null
                        )
                    }
                    VaccinationRecordSuspendedFunctions.insertVaccRec(
                        vaccRec!!,
                        requireContext()
                    )
                }
            }

        }



        firstDoseDateTV?.setOnClickListener {
            selectedDateFlag = 1
            openDialog()
        }



        Log.d("min dose", minDateInSecondDose.toString())


        secondDoseDateTV?.setOnClickListener {
            selectedDateFlag = 2
            openDialog()
        }



        return binding.root
    }

    /**
     * Formats the given date to a string representation.
     *
     * @param date The date to be formatted.
     * @return The formatted date string.
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
     * Callback method when a date is set in the DatePickerDialog.
     *
     * @param view The DatePicker view associated with the dialog.
     * @param year The selected year.
     * @param monthOfYear The selected month (0-11 for compatibility with Calendar class).
     * @param dayOfMonth The selected day of the month (1-31).
     */
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        selectedDate = java.sql.Date(calendar.timeInMillis)
        val formattedDate = formatDate(selectedDate!!)
        if (selectedDateFlag == 1) {
            firstDoseDateTV?.text = formattedDate
            minDateInSecondDose = selectedDate
            firstDate = selectedDate
        } else if (selectedDateFlag == 2) {
            secondDoseDateTV?.text = formattedDate
            secondDate = selectedDate
        }
    }

    /**
     * Format the given date into a string.
     *
     * @param date The date to format.
     * @return The formatted date string.
     */
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * Get vaccination names from the set of vaccinations read from the database.
     *
     * @param vaccinations The set of vaccinations.
     * @return An array of vaccination names.
     */
    private fun getVaccNames(vaccinations: Set<Vaccination?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccinations?.forEach { vacc ->
            vaccNames.add(vacc?.name.toString())
        }
        return vaccNames.toTypedArray()
    }

}