package com.example.lastchancedb.popups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R


//TODO:  recycle view of users future appointments of vaccination i.e. if date of the next dose > today then its there
class FutureAppointmentsFragment : DialogFragment() {

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_future_appointments, container, false)
    }

}