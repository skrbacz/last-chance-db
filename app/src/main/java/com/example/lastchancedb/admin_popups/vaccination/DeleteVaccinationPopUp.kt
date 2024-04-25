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
import com.example.lastchancedb.database.vaccination.VaccinationSuspendedFunctions
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.databinding.FragmentDeleteVaccinationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteVaccinationPopUp : DialogFragment() {

    private var _binding: FragmentDeleteVaccinationBinding? = null
    private val binding get() = _binding!!

    private var deleteBTN: Button? = null

    private val couritineScoupe = CoroutineScope(Dispatchers.Main)
    private var vaccinations: Set<Vaccination?>? = null

    override fun onResume() {
        super.onResume()

        couritineScoupe.launch {
            vaccinations = VaccinationSuspendedFunctions.getAllVaccs()
        }

        val vaccNames = getVaccNames(vaccinations)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, vaccNames)
        binding.adminVaccinationDeleteACTV.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteVaccinationBinding.inflate(inflater, container, false)
        deleteBTN = binding.adminDeleteVaccinationPOPBTN


        deleteBTN?.setOnClickListener {

            if (binding.adminVaccinationDeleteACTV.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select vaccination name first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                couritineScoupe.launch {
                    VaccinationSuspendedFunctions.deleteVacc(
                        binding.adminVaccinationDeleteACTV.text.toString(),
                        requireContext()
                    )
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
}