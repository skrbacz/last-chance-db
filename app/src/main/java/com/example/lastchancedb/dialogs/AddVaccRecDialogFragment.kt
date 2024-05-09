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

//TODO: ADD adding next dose date

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

    private var selectedDateFlag: Int = 0
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
                    Log.d("vaccRec", vaccRec.toString())
                    VaccinationRecordSuspendedFunctions.insertVaccRec(
                        vaccRec!!,
                        requireContext()
                    )
                }
            }
        }


        firstDoseDateTV?.setOnClickListener{
            selectedDateFlag = 1
            openDialog()
        }

        secondDoseDateTV?.setOnClickListener{
            selectedDateFlag = 2
            openDialog()
        }

        return binding.root
    }


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
        dialog.datePicker.firstDayOfWeek = Calendar.MONDAY
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        selectedDate = java.sql.Date(calendar.timeInMillis)
        val formattedDate = formatDate(selectedDate!!)
        if (selectedDateFlag == 1) {
            firstDoseDateTV?.text = formattedDate
            firstDate = selectedDate
        } else if (selectedDateFlag == 2) {
            secondDoseDateTV?.text = formattedDate
            secondDate = selectedDate
        }
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }


    private fun getVaccNames(vaccinations: Set<Vaccination?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccinations?.forEach { vacc ->
            vaccNames.add(vacc?.name.toString())
        }
        return vaccNames.toTypedArray()
    }
}