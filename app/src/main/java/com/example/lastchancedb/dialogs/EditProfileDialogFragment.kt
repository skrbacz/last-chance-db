package com.example.lastchancedb.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import com.example.lastchancedb.database.user.UserSuspendedFunctions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A dialog fragment for editing user profile information.
 *
 * This dialog allows users to edit their name and date of birth (DOB).
 */
class EditProfileDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var userEmail = Firebase.auth.currentUser?.email.toString()
    private var selectedDate: Date? = null
    private var dobTV: TextView? = null
    private var nameEDTV: EditText? = null
    private var submitBTN: Button? = null

    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onResume() {
        super.onResume()
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_edit_profile_dialog, container, false)

        dobTV = view.findViewById(R.id.updateDOBTV)
        nameEDTV = view.findViewById(R.id.updateNameEDTV)
        submitBTN= view.findViewById(R.id.submitProfileUpdateBTN)

        dobTV?.setOnClickListener {
            openDialog()
        }

        submitBTN?.setOnClickListener {
            val namePattern = Regex("^[a-zA-Z]+$")
            val userInput = nameEDTV?.text.toString()

            if (selectedDate == null) {
                Toast.makeText(requireContext(), "Please choose new date of birth", Toast.LENGTH_SHORT).show()
            } else if (userInput.isBlank()) {
                Toast.makeText(requireContext(), "Please enter new name", Toast.LENGTH_SHORT).show()
            } else if (!namePattern.matches(userInput)) {
                nameEDTV?.error = "Please provide a valid name"
            } else {
                coroutineScope.launch {
                    val user = UserSuspendedFunctions.getUser(userEmail)
                    user?.name = userInput
                    user?.dob = selectedDate
                    user?.let { UserSuspendedFunctions.updateUser(userEmail, it, requireContext()) }
                }
            }
        }


        return view
    }


    /**
     * Opens a date picker dialog for selecting the date of birth.
     */
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

        dobTV?.text = formattedDate
    }
    /**
     * Formats the given date into a string.
     *
     * @param date The date to format.
     * @return The formatted date string.
     */
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

}