
package com.example.lastchancedb

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lastchancedb.admin_popups.vaccination.DeleteVaccinationPopUp
import com.example.lastchancedb.admin_popups.vaccination.InsertVaccinationPopUp
import com.example.lastchancedb.admin_popups.vaccination.UpdateVaccinationPopUp
import com.example.lastchancedb.database.user.User
import com.example.lastchancedb.database.user.UserSuspendedFunctions
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminMainActivity : AppCompatActivity() {

    private lateinit var tableToggleGroup: MaterialButtonToggleGroup

    private var amountOfUsersTV: TextView? = null
    private var adminInsertBTN: Button? = null
    private var adminDeleteBTN: Button? = null
    private var adminUpdateBTN: Button? = null
    private var adminGetAllBTN: Button? = null
    private var adminGetOneBTN: Button? = null

    private val couritineScoupe = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)

        tableToggleGroup = findViewById(R.id.toggleButtonGroup)
        amountOfUsersTV = findViewById(R.id.amountOfUsersTVADMIN)
        adminInsertBTN = findViewById(R.id.adminInsertBTN)
        adminDeleteBTN = findViewById(R.id.adminDeleteBTN)
        adminUpdateBTN = findViewById(R.id.adminUpdateBTN)
        adminGetAllBTN = findViewById(R.id.adminGetAllBTN)
        adminGetOneBTN = findViewById(R.id.adminGetOneBTN)

        amountOfUsersTV?.text = getAmountOfUsers()


        tableToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.usersBTN -> {
                        adminInsertBTN?.visibility = View.INVISIBLE
                        adminDeleteBTN?.visibility = View.INVISIBLE
                        adminUpdateBTN?.visibility = View.INVISIBLE
                        adminGetAllBTN?.visibility = View.INVISIBLE
                        adminGetOneBTN?.visibility = View.INVISIBLE
                        amountOfUsersTV?.visibility = View.INVISIBLE
                    }

                    R.id.vaccinationsBTN -> {
                        Toast.makeText(this, "vaccinations", Toast.LENGTH_SHORT).show()
                        adminInsertBTN?.setOnClickListener {
                            val showPopUp= InsertVaccinationPopUp()
                            showPopUp.show(supportFragmentManager, "InsertVaccinationPopUp")
                        }

                        adminDeleteBTN?.setOnClickListener {
                            val showPopUp= DeleteVaccinationPopUp()
                            showPopUp.show(supportFragmentManager, "DeleteVaccinationPopUp")
                        }

                        adminUpdateBTN?.setOnClickListener {
                            val showPopUp= UpdateVaccinationPopUp()
                            showPopUp.show(supportFragmentManager, "UpdateVaccinationPopUp")
                        }

                        adminGetAllBTN?.setOnClickListener {
                            Toast.makeText(this, "get all vaccinations- coming soon", Toast.LENGTH_SHORT).show()
                        }

                        adminGetOneBTN?.setOnClickListener {
                            Toast.makeText(this, "get one vaccination- coming soon", Toast.LENGTH_SHORT).show()
                        }
                    }

                    R.id.vaccinationRecordsBTN -> {
                        Toast.makeText(this, "vaccinationRecords", Toast.LENGTH_SHORT).show()
                        adminInsertBTN?.visibility = View.INVISIBLE
                        adminDeleteBTN?.visibility = View.INVISIBLE
                        adminUpdateBTN?.visibility = View.INVISIBLE
                        adminGetAllBTN?.visibility = View.INVISIBLE
                        adminGetOneBTN?.visibility = View.INVISIBLE
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Choose on which table you want to operate",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }

    private fun getAmountOfUsers(): String {
        var users: Set<User?>? = null
        couritineScoupe.launch {
            users= UserSuspendedFunctions.getAllUsers()
        }
        return users!!.size.toString()
    }

}

