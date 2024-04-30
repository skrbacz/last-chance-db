
package com.example.lastchancedb

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lastchancedb.admin_popups.user.DeleteUserPopUp
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

    private var toggleBtn: MaterialButtonToggleGroup?=null

    private var vaccsBtn: Button?=null
    private var usersBtn: Button?=null
    private var vaccRecsBtn: Button?=null
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

        toggleBtn = findViewById(R.id.toggleButtonGroup)

        amountOfUsersTV = findViewById(R.id.amountOfUsersTVADMIN)
        adminInsertBTN = findViewById(R.id.adminInsertBTN)
        adminDeleteBTN = findViewById(R.id.adminDeleteBTN)
        adminUpdateBTN = findViewById(R.id.adminUpdateBTN)
        adminGetAllBTN = findViewById(R.id.adminGetAllBTN)
        adminGetOneBTN = findViewById(R.id.adminGetOneBTN)

        usersBtn = findViewById(R.id.usersBTN)
        vaccsBtn = findViewById(R.id.vaccinationsBTN)
        vaccRecsBtn = findViewById(R.id.vaccinationRecordsBTN)

        invisibleButtons()

        couritineScoupe.launch {
            amountOfUsersTV?.text = getAmountOfUsers()
        }

        usersBtn?.setOnClickListener {
            invisibleButtons()
            adminDeleteBTN?.visibility = View.VISIBLE
            adminDeleteBTN?.setOnClickListener {
                val showPopUp= DeleteUserPopUp()
                showPopUp.show(supportFragmentManager, "DeleteUserPopUp")
            }
        }

        vaccsBtn?.setOnClickListener {
            visibleButtons()
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
                Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show()
            }

            adminGetOneBTN?.setOnClickListener {
                Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show()
            }
        }

        vaccRecsBtn?.setOnClickListener {
            invisibleButtons()
            Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show()
        }


    }

    private fun getAmountOfUsers(): String {
        var users: Set<User?>? = null
        couritineScoupe.launch {
            users= UserSuspendedFunctions.getAllUsers()
        }
        val size = users?.size ?: 0
        return size.toString()
    }

    private fun invisibleButtons(){
        adminInsertBTN?.visibility = View.INVISIBLE
        adminDeleteBTN?.visibility = View.INVISIBLE
        adminUpdateBTN?.visibility = View.INVISIBLE
        adminGetAllBTN?.visibility = View.INVISIBLE
        adminGetOneBTN?.visibility = View.INVISIBLE
    }

    private fun visibleButtons(){
        adminInsertBTN?.visibility = View.VISIBLE
        adminDeleteBTN?.visibility = View.VISIBLE
        adminUpdateBTN?.visibility = View.VISIBLE
        adminGetAllBTN?.visibility = View.VISIBLE
        adminGetOneBTN?.visibility = View.VISIBLE
    }

}

