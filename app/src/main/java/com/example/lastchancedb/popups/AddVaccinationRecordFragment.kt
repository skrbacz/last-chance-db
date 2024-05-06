package com.example.lastchancedb.popups

import android.app.DatePickerDialog
import android.os.Bundle
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
import com.example.lastchancedb.databinding.FragmentAddVaccinationBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.sql.Date
import java.util.Calendar
import java.util.Locale

class AddVaccinationRecordFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddVaccinationBinding? = null
    private val binding get() = _binding!!

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var vaccinations: Set<Vaccination?>? = null
    private var addBtn: Button? = null
    private var dateBtn: TextView?=null

    private var selectedDate: Date?= null
    private var userEmail: String? = null

    override fun onResume() {
        super.onResume()

        coroutineScope.launch {
            vaccinations = VaccinationSuspendedFunctions.getAllVaccs()

            binding.apply {
                val vaccNames = getVaccNames(vaccinations)
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, vaccNames)
                vaccinationNameForRecordACTV.setAdapter(arrayAdapter)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddVaccinationBinding.inflate(inflater, container, false)

        addBtn= binding.addVaccRecBTN
        dateBtn=binding.dateText

        dateBtn?.setOnClickListener {
            openDialog()
        }

//        userEmail = getCurrentUser()
        userEmail="test@test.com"

        addBtn?.setOnClickListener {
            if(binding.vaccinationNameForRecordACTV.text.isEmpty()){
                Toast.makeText(requireContext(), "Please choose a vaccination", Toast.LENGTH_SHORT).show()
            }else if (selectedDate == null) {
                Toast.makeText(requireContext(), "Please choose a date", Toast.LENGTH_SHORT).show()
            }else {
                coroutineScope.launch{
                    val vaccRec= selectedDate?.let { it1 ->
                        VaccinationRecord(null,userEmail,binding.vaccinationNameForRecordACTV.text.toString(),
                            it1,null)
                    }
                    VaccinationRecordSuspendedFunctions.insertVaccRec(vaccRec!!,requireContext())
                }
            }
        }


        return binding.root
    }

    private fun openDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        dialog.show()
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


    private fun getVaccNames(vaccinations: Set<Vaccination?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccinations?.forEach { vacc ->
            vaccNames.add(vacc?.name.toString())
        }
        return vaccNames.toTypedArray()
    }

//    fun getCurrentUser(): String{
//        val auth = FirebaseAuth.getInstance()
//        return auth.currentUser?.email.toString()
//    }

}