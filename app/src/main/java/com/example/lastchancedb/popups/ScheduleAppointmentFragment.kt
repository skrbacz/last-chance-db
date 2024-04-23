package com.example.lastchancedb.popups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R

class ScheduleAppointmentFragment(private var dose: String) : DialogFragment() {

    private var tv: TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_appointment, container, false)

        tv = view.findViewById(R.id.textView)
        if (dose == "default") {
            tv?.text= R.string.whatever.toString()
        }else{
            tv?.text = dose
        }
        return view
    }

}
