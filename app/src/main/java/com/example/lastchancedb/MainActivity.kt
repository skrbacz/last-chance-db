package com.example.lastchancedb

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.popups.AddVaccinationRecordFragment
import com.example.lastchancedb.popups.QuestionScheduleAppointmentFragment
import com.example.lastchancedb.recycler_view_activities.UserVaccRecStorageActivity
import com.example.lastchancedb.recycler_view_activities.VaccinationLibraryActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.util.Calendar
import kotlin.math.abs

//TODO: get the closest by date record to make circular progress bar work and display its name and date
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
    private var progressBar: CircularProgressBar?= null

    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<LinearLayout>(R.id.bottom_navigation)

        val buttonHome = bottomNavigationView.findViewById<ImageView>(R.id.button_home)
        val buttonLibrary = bottomNavigationView.findViewById<ImageView>(R.id.button_library)
        val buttonStorage = bottomNavigationView.findViewById<ImageView>(R.id.button_storage)
        val clicked = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_pink))

        buttonHome.imageTintList = clicked

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
            val showPopUp = QuestionScheduleAppointmentFragment()
            showPopUp.show(supportFragmentManager, "QuestionScheduleAppointmentFragment")
        }


        addVaccinationBtn?.setOnClickListener {
            val showPopUp = AddVaccinationRecordFragment()
            showPopUp.show(supportFragmentManager, "CalendarPopUp")
        }

        vaccLib?.setOnClickListener {
            val intent = Intent(this@MainActivity, VaccinationLibraryActivity::class.java)
            startActivity(intent)
        }

        progressBar= findViewById(R.id.progressBar)



    }

    private fun daysUntilNextDose(): Int {

        return 20

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

    fun findClosestVaccinationRecord(vaccinationRecords: Set<VaccinationRecord?>?): VaccinationRecord? {
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
        }

        return closestRecord
    }

}

