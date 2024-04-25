package com.example.lastchancedb.admin_popups.vaccination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.database.vaccination.VaccinationSuspendedFunctions
import com.example.lastchancedb.databinding.FragmentDeleteVaccinationBinding
import com.example.lastchancedb.databinding.FragmentUpdateVaccinationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UpdateVaccinationPopUp : DialogFragment() {

    private var updateBTN: Button? = null

    private var _binding: FragmentUpdateVaccinationBinding? = null
    private val binding get() = _binding!!
    private val couritineScoupe = CoroutineScope(Dispatchers.Main)
    private var vaccinations: Set<Vaccination?>? = null

    override fun onResume() {
        super.onResume()

        couritineScoupe.launch {
            vaccinations = VaccinationSuspendedFunctions.getAllVaccs()
        }

        val vaccName = getVaccNames(vaccinations)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, vaccName)
        binding.adminVaccinationUpdateACTV.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateVaccinationBinding.inflate(inflater, container, false)

        updateBTN = binding.adminUpdateVaccinationPOPBTN


        updateBTN?.setOnClickListener {

            if (binding.adminVaccinationUpdateACTV.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select vaccination name first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (validInputs()) {
                    val name = binding.adminVaccinationUpdateACTV.text.toString()
                    val interval = binding.updateIntervalEDTV.text.toString().toInt()
                    val shortDescrtiption = binding.updateShortDescriptionEDTV.text.toString()
                    val vaccination = Vaccination(name, interval, shortDescrtiption)
                    couritineScoupe.launch {
                        VaccinationSuspendedFunctions.updateVacc(
                            name,
                            vaccination,
                            requireContext()
                        )
                    }
                }
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

    private fun validInputs(): Boolean {
        if (binding.updateIntervalEDTV.text.isNullOrBlank()) {
            binding.updateIntervalEDTV.error = "Interval cannot be empty"
            return false
        } else if (binding.updateShortDescriptionEDTV.text.isNullOrBlank()) {
            binding.updateShortDescriptionEDTV.error = "Short description cannot be empty"
            return false
        }
        return true
    }


}