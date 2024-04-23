
package com.example.lastchancedb

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lastchancedb.popups.QuestionScheduleAppointmentFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_animation)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_animation)}

    private var addButton: FloatingActionButton?=null
    private var addVaccinationBtn: FloatingActionButton?=null
    private var scheduleVaccinationBtn: FloatingActionButton?=null

    private var clicked = false

    private val couritineScoupe= CoroutineScope(Dispatchers.Main)
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton= findViewById(R.id.addBtn)
        addVaccinationBtn= findViewById(R.id.addVaccinationBtn)
        scheduleVaccinationBtn= findViewById(R.id.scheduleVaccinationBtn)


         addButton?.setOnClickListener {
             onAddButtonClicked()
         }

         scheduleVaccinationBtn?.setOnClickListener {
            val showPopUp= QuestionScheduleAppointmentFragment()
            showPopUp.show(supportFragmentManager,"QuestionScheduleAppointmentFragment")
         }


         addVaccinationBtn?.setOnClickListener {
             Toast.makeText(this, "TODO: Add Vaccination", Toast.LENGTH_SHORT).show()
         }




//        executeBTN = findViewById(R.id.executeBTN)
//        vaccNameED = findViewById(R.id.vaccNameED)
//        daysUntilNextDoseED = findViewById(R.id.daysUntilNextDoseED)
//        descriptionED = findViewById(R.id.descriptionED)
//
//        executeBTN?.setOnClickListener {
//
//            val vaccName = vaccNameED?.text.toString()
//            val daysUntilNextDose = daysUntilNextDoseED?.text.toString().toInt()
//            val description = descriptionED?.text.toString()
//            val vacc= Vaccination(vaccName,daysUntilNextDose,description)
//
//            couritineScoupe.launch {
//                insertVacc(vacc)
//            }
//        }
//    }
//    private suspend fun insertVacc(vaccination: Vaccination) {
//        withContext(Dispatchers.IO){
//            val connection= DatabaseConnection.getConnection()
//            val userQueries = VaccQueries(connection)
//            val result = userQueries.insertVacc(vaccination)
//            connection.close()
//
//            withContext(Dispatchers.Main){
//                if (result) {
//                    Toast.makeText(this@MainActivity,"Vaccination inserted",Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(this@MainActivity,"Vaccination insertion failed",Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked:Boolean) {
       if(!clicked){
           addVaccinationBtn?.visibility= View.VISIBLE
           scheduleVaccinationBtn?.visibility= View.VISIBLE
       }else{
           addVaccinationBtn?.visibility= View.INVISIBLE
           scheduleVaccinationBtn?.visibility= View.INVISIBLE
       }
    }
    private fun setAnimation(clicked:Boolean) {
        if(!clicked){
            addButton?.startAnimation(rotateOpen)
            scheduleVaccinationBtn?.startAnimation(fromBottom)
            addVaccinationBtn?.startAnimation(fromBottom)
        }else{
            addButton?.startAnimation(rotateClose)
            scheduleVaccinationBtn?.startAnimation(toBottom)
            addVaccinationBtn?.startAnimation(toBottom)
        }
    }
}
