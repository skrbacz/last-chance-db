package com.example.lastchancedb.admin_popups.vaccination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.database.vaccination.VaccinationSuspendedFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class InsertVaccinationPopUp : DialogFragment() {

    private var nameEDTV: EditText?=null
    private var daysUntilNextDose: EditText?=null
    private var shortDescriptionEDTV: EditText?=null
    private var insertBTN: Button?=null

    private val couritineScoupe = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=  inflater.inflate(R.layout.fragment_insert_vaccination, container, false)

        insertBTN= view.findViewById(R.id.button)
        nameEDTV=view.findViewById(R.id.vaccNameEDTV)
        daysUntilNextDose=view.findViewById(R.id.intervalEDTV)
        shortDescriptionEDTV=view.findViewById(R.id.shortDescriptionEDTV)

        insertBTN?.setOnClickListener {
            if(validInputs()){

                val name= nameEDTV?.text.toString().toUpperCase(Locale.ROOT)
                val interval= daysUntilNextDose?.text.toString().toInt()
                val shortDescription= shortDescriptionEDTV?.text.toString()

                val vaccination = Vaccination(name,interval,shortDescription)

                couritineScoupe.launch {
                    VaccinationSuspendedFunctions.insertVacc(vaccination,requireContext())
                }
            }
        }


        return view
    }

    private fun validInputs(): Boolean {
        if(nameEDTV?.text.isNullOrBlank()){
            nameEDTV?.error="Name cannot be empty"
            return false
        }else if(daysUntilNextDose?.text.isNullOrBlank()){
            daysUntilNextDose?.error="Interval cannot be empty"
            return false
        }else if(shortDescriptionEDTV?.text.isNullOrBlank()){
            shortDescriptionEDTV?.error="Short description cannot be empty"
            return false
        }
        return true
    }
}