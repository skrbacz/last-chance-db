package com.example.lastchancedb.popups

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//TODO: make it work for next dose: calendar starts at recommended day
class SaveAppointmentFragment(private var dose: String) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {


    private var _binding: FragmentSaveAppointmentBinding? = null
    private val binding get() = _binding!!

    private var scheduleBTN: Button? = null
    private var dateBtn: TextView? = null
    private var date: Date? = null
    private var userEmail= Firebase.auth.currentUser?.email.toString()


    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var vaccinations: Set<Vaccination?>? = null
    private var vaccRecs: Set<VaccinationRecord?>? = null
    private var selectedDate: Date? = null
    private var nexDoseDate: Calendar? = null

    override fun onResume() {
        super.onResume()

        coroutineScope.launch {
            if (dose == "First dose") {
                vaccinations = VaccinationSuspendedFunctions.getAllVaccs()
                binding.apply {
                    val vaccNames = getVaccNamesFromVacc(vaccinations)
                    val arrayAdapter =
                        ArrayAdapter(requireContext(), R.layout.drop_down_item, vaccNames)
                    vaccNameForAppointmentACTV.setAdapter(arrayAdapter)
                }
            } else if (dose == "Next dose") {
                vaccRecs =
                    VaccinationRecordSuspendedFunctions.getAllVaccRecByUserEmail(userEmail.toString())
                binding.apply {
                    val vaccNames = getVaccNamesFromVaccRec(vaccRecs)
                    val arrayAdapter =
                        ArrayAdapter(requireContext(), R.layout.drop_down_item, vaccNames)
                    vaccNameForAppointmentACTV.setAdapter(arrayAdapter)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveAppointmentBinding.inflate(inflater, container, false)

        scheduleBTN = binding.scheduleAppointmentBTN

        dateBtn= binding.dateTV


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
            }else {
                coroutineScope.launch {
                    if (dose == "First dose") {
                        val vaccRec = date?.let { it1 ->
                            VaccinationRecord(
                                null, userEmail, binding.vaccNameForAppointmentACTV.text.toString(),
                                it1, null
                            )
                        }
                        VaccinationRecordSuspendedFunctions.insertVaccRec(
                            vaccRec!!,
                            requireContext()
                        )
                    } else if (dose == "Next dose") {
                        val vaccRec =
                            VaccinationRecordSuspendedFunctions.getVaccRecByUserEmailVaccName(
                                userEmail,
                                binding.vaccNameForAppointmentACTV.text.toString()
                            )
                        vaccRec?.nextDoseDate = date
                        vaccRec!!.id?.let { it1 ->
                            VaccinationRecordSuspendedFunctions.updateVaccRec(
                                it1, vaccRec, requireContext()
                            )
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun getVaccNamesFromVacc(vaccinations: Set<Vaccination?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccinations?.forEach { vacc ->
            vaccNames.add(vacc?.name.toString())
        }
        return vaccNames.toTypedArray()
    }

    private fun getVaccNamesFromVaccRec(vaccRecs: Set<VaccinationRecord?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccRecs?.forEach { vaccRec ->
            vaccNames.add(vaccRec?.vaccName.toString())
        }
        return vaccNames.toTypedArray()
    }

    private fun openDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        if(dose == "First dose") {
            dialog.datePicker.minDate = currentDate.timeInMillis
        }else if(dose == "Next dose"){
            dialog.datePicker.minDate = nexDoseDate?.timeInMillis ?: currentDate.timeInMillis
        }
        dialog.show()
    }

    private suspend fun recommendedDate(vaccinationName: String): Calendar? {
        // Fetch vaccination record asynchronously
        val vaccinationRecord = coroutineScope.async(Dispatchers.IO) {
            VaccinationRecordSuspendedFunctions.getVaccRecByUserEmailVaccName(userEmail, vaccinationName)
        }.await()

        val firstDose = vaccinationRecord?.dateAdministrated

        return firstDose?.let { date ->
            val vaccination = withContext(Dispatchers.IO) {
                VaccinationSuspendedFunctions.getVacc(vaccinationName)
            }
            val recommendedDate = Calendar.getInstance().apply { time = date }
            val interval = vaccination?.daysUntilNextDose ?: 0
            recommendedDate.add(Calendar.DAY_OF_MONTH, interval)
            Log.d("recommendedDate in popup", recommendedDate.toString())
            recommendedDate
        }
    }

    private fun calculateAndSetRecommendedDate(vaccinationName: String) {
        coroutineScope.launch {
            val recommendedDate = recommendedDate(vaccinationName)
            // Set the recommended date to the UI element
            recommendedDate?.let { setRecommendedDateToUI(it) }
        }
    }

    private fun setRecommendedDateToUI(recommendedDate: Calendar) {
        val formattedDate = formatDate(Date(recommendedDate.timeInMillis))
        dateBtn?.text = formattedDate
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        selectedDate = java.sql.Date(calendar.timeInMillis)
        val formattedDate = formatDate(selectedDate!!) // Format selected date
        dateBtn?.text = formattedDate
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }



}
