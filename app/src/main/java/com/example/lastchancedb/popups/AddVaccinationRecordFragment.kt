package com.example.lastchancedb.popups

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.database.vaccination.VaccinationSuspendedFunctions
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.databinding.FragmentAddVaccinationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date

//TODO: transfer data from calendar fragment to this
class AddVaccinationRecordFragment : DialogFragment() {

    private var _binding: FragmentAddVaccinationBinding? = null
    private val binding get() = _binding!!

    private val couritineScoupe = CoroutineScope(Dispatchers.Main)
    private var vaccinations: Set<Vaccination?>? = null
    private var addBtn: Button? = null
    private var dateBtn: TextView?=null

    override fun onResume() {
        super.onResume()

        couritineScoupe.launch {
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
            val showPopUp= CalendarFragmentPopUp()
            showPopUp.show(parentFragmentManager, "CalendarFragmentPopUp")
        }


        addBtn?.setOnClickListener {
            if(binding.vaccinationNameForRecordACTV.text.isEmpty()){
                Toast.makeText(requireContext(), "Please choose a vaccination", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    private fun getVaccNames(vaccinations: Set<Vaccination?>?): Array<String> {
        val vaccNames = mutableListOf<String>()
        vaccinations?.forEach { vacc ->
            vaccNames.add(vacc?.name.toString())
        }
        return vaccNames.toTypedArray()
    }

}