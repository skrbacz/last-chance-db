package com.example.lastchancedb.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.database.vaccination.VaccinationSuspendedFunctions
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.database.vaccination_record.VaccinationRecordSuspendedFunctions
import com.example.lastchancedb.databinding.FragmentSaveAppointmentBinding
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
 * Dialog fragment for saving appointments for the first dose or next dose of vaccination.
 *
 * @param dose The type of dose to schedule the appointment for.
 */
class SaveAppointmentDialogFragment(private var dose: String) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentSaveAppointmentBinding? = null
    private val binding get() = _binding!!

    private var scheduleBTN: Button? = null

    private var dateBtn: TextView? = null

    private var selectedDate: Date? = null

    private var userEmail = Firebase.auth.currentUser?.email.toString()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var recommendedDate: java.sql.Date?= null

    /**
     * Called when the fragment is resumed. This method sets the dialog window dimensions
     * and initializes the dropdown list based on the type of dose.
     */
    override fun onResume() {
        super.onResume()
        val params= dialog?.window?.attributes
        params?.width= ViewGroup.LayoutParams.MATCH_PARENT
        params?.height= ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes=params

        coroutineScope.launch {
            if (dose == "First dose") {
                initFirstDose()
            } else if (dose == "Next dose") {
                initNextDose()
            }
        }
    }
    /**
     * Initializes the dropdown list with available vaccinations for the first dose.
     */
    private suspend fun initFirstDose() {
        val vaccinations = VaccinationSuspendedFunctions.getAllVaccs()
        updateDropDownList(getVaccNamesFromVacc(vaccinations))
    }
    /**
     * Initializes the dropdown list with vaccinations that have no scheduled next dose.
     */
    private suspend fun initNextDose() {
        val vaccRecs = VaccinationRecordSuspendedFunctions.getAllVaccRecByUserEmail(userEmail)
        val vaccRecNoNextDose = vaccRecs
            ?.filter { it?.nextDoseDate == null }
            ?.toSet()
        updateDropDownList(getVaccNamesFromVaccRec(vaccRecNoNextDose))
    }


    /**
     * Updates the dropdown list with the given array of vaccination names.
     */
    private fun updateDropDownList(names: Array<String>) {
        binding.apply {
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, names)
            vaccNameForAppointmentACTV.setAdapter(arrayAdapter)
        }
    }
    /**
     * Retrieves the vaccination names from the given list of vaccinations.
     */
    private fun getVaccNamesFromVacc(vaccinations: Set<Vaccination?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccinations?.forEach { vacc ->
            vaccNames.add(vacc?.name.toString())
        }
        return vaccNames.toTypedArray()
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
    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveAppointmentBinding.inflate(inflater, container, false)

        scheduleBTN = binding.scheduleAppointmentBTN
        dateBtn = binding.dateTV

        binding.vaccNameForAppointmentACTV.setOnItemClickListener { parent, view, position, id ->
            coroutineScope.launch {
                val vaccName = binding.vaccNameForAppointmentACTV.text.toString()
                val vaccRec = if (dose == "First dose") {
                    null
                } else {
                    VaccinationRecordSuspendedFunctions.getVaccRecByUserEmailVaccName(userEmail, vaccName)
                }

                val vacc = if (dose == "First dose") {
                    null
                } else {
                    VaccinationSuspendedFunctions.getVacc(vaccName)
                }
                vacc?.daysUntilNextDose?.let { getRecommendedDate(vaccRec, it) }
            }
        }

        dateBtn?.setOnClickListener {
            openDialog()
        }

        scheduleBTN?.setOnClickListener {
            if (binding.vaccNameForAppointmentACTV.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select vaccination name first",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (selectedDate == null) {
                Toast.makeText(
                    requireContext(),
                    "Please select date first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (dose == "First dose") {
                    coroutineScope.launch {
                        val vaccRec = selectedDate?.let { it1 ->
                            VaccinationRecord(
                                null,
                                userEmail,
                                binding.vaccNameForAppointmentACTV.text.toString(),
                                it1,
                                null
                            )
                        }
                        Log.d("vacc rec before insert in save", vaccRec.toString())
                        if (vaccRec != null) {
                            VaccinationRecordSuspendedFunctions.insertVaccRec(
                                vaccRec,
                                requireContext()
                            )
                        }
                    }
                } else if (dose == "Next dose") {
                    coroutineScope.launch {
                        val vaccRec =
                            VaccinationRecordSuspendedFunctions.getVaccRecByUserEmailVaccName(
                                userEmail,
                                binding.vaccNameForAppointmentACTV.text.toString()
                            )

                        Log.d("vacc rec before update in save", vaccRec.toString())
                        vaccRec?.nextDoseDate = selectedDate
                        Log.d("updated vacc rec", vaccRec.toString())

                        vaccRec?.id?.let { it1 ->
                            VaccinationRecordSuspendedFunctions.updateVaccRec(
                                it1, vaccRec, requireContext())
                        }
                    }
                }
            }
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

        val dialog = DatePickerDialog(requireContext(), R.style.CustomCalendarDialogTheme, this, year, month, day)
        dialog.datePicker.firstDayOfWeek = Calendar.MONDAY

        // Set minimum date based on the type of dose
        if (dose == "First dose") {
            dialog.datePicker.minDate = currentDate.timeInMillis
        } else if (dose == "Next dose") {
            if (binding.vaccNameForAppointmentACTV.text.isNotEmpty()) {
                Log.d("recommendedDate", recommendedDate.toString())
                dialog.datePicker.minDate = recommendedDate?.time ?: currentDate.timeInMillis
            } else {
                dialog.datePicker.minDate = currentDate.timeInMillis
                Toast.makeText(
                    requireContext(),
                    "Please select vaccination name first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
        dateBtn?.text = formattedDate
    }

    /**
     * Formats the given date to a string representation.
     *
     * @param date The date to be formatted.
     * @return The formatted date string.
     */
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }
    /**
     * Calculates the recommended date for the next dose appointment based on the
     * average interval between doses and the date of administration of the previous dose.
     */
    private fun getRecommendedDate(vaccinationRecord: VaccinationRecord?, averageInterval: Int) {
        val adminDateMillis = vaccinationRecord?.dateAdministrated?.time ?: 0L
        val daysUntilNextDose = averageInterval
        val nextDoseMillis = daysUntilNextDose * 24 * 60 * 60 * 1000L  // Convert days to milliseconds
        val sum = adminDateMillis + nextDoseMillis
        recommendedDate= Date(sum)
    }
}
