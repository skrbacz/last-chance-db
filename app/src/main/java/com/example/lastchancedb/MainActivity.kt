package com.example.lastchancedb

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.database.vaccination_record.VaccinationRecordSuspendedFunctions
import com.example.lastchancedb.dialogs.QuestionAddOrUpdateVaccRecDialogFragment
import com.example.lastchancedb.dialogs.QuestionSaveAppointmentDialogFragment
import com.example.lastchancedb.other_activities.ProfileActivity
import com.example.lastchancedb.other_activities.UserVaccRecStorageActivity
import com.example.lastchancedb.other_activities.VaccinationLibraryActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_animation
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_animation
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_animation
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_animation
        )
    }

    private var addButton: FloatingActionButton? = null
    private var addVaccinationBtn: FloatingActionButton? = null
    private var scheduleVaccinationBtn: FloatingActionButton? = null
    private var goToAdmin: Button? = null
    private var vaccLib: Button? = null
    private var profile: Button? = null
    private var progressBar: CircularProgressBar?= null

    private var clicked = false

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var vaccinationRecords: Set<VaccinationRecord?>? = null
    private var userEmail = Firebase.auth.currentUser?.email.toString()

    private var closestVaccDate: java.sql.Date?= null
    private var closestVaccDateString: String?= null
    private var closestVaccName: String?= null

    private var closestVaccNameTV: TextView?= null
    private var closestVaccDateTV: TextView?= null
    private var daysLeftTV: TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        closestVaccDateTV= findViewById(R.id.appointmentDateTV)
        closestVaccNameTV= findViewById(R.id.vaccinationNameTV)
        daysLeftTV=findViewById(R.id.daysView)


        val bottomNavigationView = findViewById<LinearLayout>(R.id.bottom_navigation)

        val buttonProfile= bottomNavigationView.findViewById<ImageView>(R.id.button_profile)
        val buttonHome = bottomNavigationView.findViewById<ImageView>(R.id.button_home)
        val buttonLibrary = bottomNavigationView.findViewById<ImageView>(R.id.button_library)
        val buttonStorage = bottomNavigationView.findViewById<ImageView>(R.id.button_storage)
        val clicked = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_pink))

        progressBar= findViewById(R.id.progressBar)


        coroutineScope.launch {
            vaccinationRecords = VaccinationRecordSuspendedFunctions.getAllVaccRecByUserEmail(userEmail)
            findClosestVaccinationRecord(vaccinationRecords)

            closestVaccDateTV?.text = closestVaccDateString
            closestVaccNameTV?.text = closestVaccName

            closestVaccDate?.let { closestDate ->
                val currentDate = Date()
                val differenceDays = daysBetween(currentDate, closestDate)
                val daysLeft = max(differenceDays.toInt(), 0) // Ensure daysLeft is not negative
                Log.d("differenceDays", differenceDays.toString())


                daysLeftTV?.text = daysLeft.toString()
                val progress = (differenceDays - daysLeft.toFloat()) / differenceDays
                progressBar?.apply {
                    progressMax = 1f
                    setProgressWithAnimation(progress, 2000) // Adjust animation duration as needed
                }
            }
        }



        buttonHome.imageTintList = clicked

        buttonProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        buttonLibrary.setOnClickListener {
            val intent = Intent(this@MainActivity, VaccinationLibraryActivity::class.java)
            startActivity(intent)
        }

        buttonStorage.setOnClickListener {
            val intent = Intent(this@MainActivity, UserVaccRecStorageActivity::class.java)
            startActivity(intent)
        }

        addButton = findViewById(R.id.addBtn)
        addVaccinationBtn = findViewById(R.id.addVaccinationBtn)
        scheduleVaccinationBtn = findViewById(R.id.scheduleVaccinationBtn)

        addButton?.setOnClickListener {
            onAddButtonClicked()
        }

        scheduleVaccinationBtn?.setOnClickListener {
            val showPopUp = QuestionSaveAppointmentDialogFragment()
            showPopUp.show(supportFragmentManager, "QuestionScheduleAppointmentFragment")
        }


        addVaccinationBtn?.setOnClickListener {
            val showPopUp = QuestionAddOrUpdateVaccRecDialogFragment()
            showPopUp.show(supportFragmentManager, "CalendarPopUp")
        }

        vaccLib?.setOnClickListener {
            val intent = Intent(this@MainActivity, VaccinationLibraryActivity::class.java)
            startActivity(intent)
        }


    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            addVaccinationBtn?.visibility = View.VISIBLE
            scheduleVaccinationBtn?.visibility = View.VISIBLE
        } else {
            addVaccinationBtn?.visibility = View.INVISIBLE
            scheduleVaccinationBtn?.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            addButton?.startAnimation(rotateOpen)
            scheduleVaccinationBtn?.startAnimation(fromBottom)
            addVaccinationBtn?.startAnimation(fromBottom)
        } else {
            addButton?.startAnimation(rotateClose)
            scheduleVaccinationBtn?.startAnimation(toBottom)
            addVaccinationBtn?.startAnimation(toBottom)
        }
    }

    fun findClosestVaccinationRecord(vaccinationRecords: Set<VaccinationRecord?>?) {
        val currentDate = Calendar.getInstance()
        var closestRecord: VaccinationRecord? = null
        var minDifference = Long.MAX_VALUE

        if (vaccinationRecords != null) {
            for (record in vaccinationRecords) {
                val difference = abs((record?.nextDoseDate?.time ?: -1 ) - currentDate.timeInMillis)
                if (difference < minDifference) {
                    minDifference = difference
                    closestRecord = record
                }
            }
            closestVaccName = closestRecord?.vaccName
            closestVaccDate = closestRecord?.nextDoseDate
            closestVaccDateString = closestVaccDate?.let { formatDate(it) }
        }else{
            closestVaccName= "N/A"
            closestVaccDateString= "N/A"
            daysLeftTV?.text = "--"
        }




    }
    private fun formatDate(date: java.sql.Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    fun daysBetween(startDate: Date, endDate: Date): Float {
        val differenceMillis = endDate.time - startDate.time
        return differenceMillis / (1000f * 60 * 60 * 24)
    }


}

