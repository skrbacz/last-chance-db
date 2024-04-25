package com.example.lastchancedb.admin_popups.vaccination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R

//TODO: MAKE LAYOUT
class GetAllVaccinationsPopUp : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_get_all_vaccinations_pop_up, container, false)




        return view
    }


}