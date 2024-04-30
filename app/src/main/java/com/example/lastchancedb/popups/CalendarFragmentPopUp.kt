package com.example.lastchancedb.popups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarFragmentPopUp : DialogFragment() {

    private var selectedDate: String? = null
    private var calendarView: CalendarView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_calendar_pop_up, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        calendarView?.minDate = currentTimeMillis()

        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)


        }

        return view
    }

}