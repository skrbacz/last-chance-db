package com.example.lastchancedb.other_activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lastchancedb.MainActivity
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.database.vaccination_record.VaccinationRecordSuspendedFunctions
import com.example.lastchancedb.other_activities.adapters.UserVaccRecStorageAdapter
import com.example.lastchancedb.other_activities.models.VaccRecModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//TODO: adding/ updating
class UserVaccRecStorageActivity : AppCompatActivity(), UserVaccRecStorageAdapter.OnDeleteClickListener {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var vaccinationRecords: Set<VaccinationRecord?>? = null
    private var vaccRecModels: ArrayList<VaccRecModel>? = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var userEmail = Firebase.auth.currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_users_history)

        val bottomNavigationView = findViewById<LinearLayout>(R.id.bottom_navigation)

        val buttonProfile= bottomNavigationView.findViewById<ImageView>(R.id.button_profile)
        val buttonHome = bottomNavigationView.findViewById<ImageView>(R.id.button_home)
        val buttonLibrary = bottomNavigationView.findViewById<ImageView>(R.id.button_library)
        val buttonStorage = bottomNavigationView.findViewById<ImageView>(R.id.button_storage)
        val clicked = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_pink))


        buttonStorage.imageTintList = clicked

        buttonHome.setOnClickListener {
            val intent = Intent(this@UserVaccRecStorageActivity, MainActivity::class.java)
            startActivity(intent)
        }

        buttonLibrary.setOnClickListener {
            val intent =
                Intent(this@UserVaccRecStorageActivity, VaccinationLibraryActivity::class.java)
            startActivity(intent)
        }

        buttonProfile.setOnClickListener {
            val intent = Intent(this@UserVaccRecStorageActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById<RecyclerView>(R.id.vaccHistoryRV)
        coroutineScope.launch {
            vaccinationRecords =
                VaccinationRecordSuspendedFunctions.getAllVaccRecByUserEmail(userEmail)
            setUpVaccRecModel(vaccinationRecords)
            vaccRecModels?.let {
                recyclerView?.adapter =
                    UserVaccRecStorageAdapter(
                        this@UserVaccRecStorageActivity,
                        it,
                        this@UserVaccRecStorageActivity
                    )
                recyclerView?.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this@UserVaccRecStorageActivity)
            }
        }
    }

    private fun setUpVaccRecModel(vaccinationRecords: Set<VaccinationRecord?>?) {
        vaccinationRecords?.forEach { vaccRec ->
            val vaccName = vaccRec?.vaccName ?: ""
            val vaccId = vaccRec?.id ?: -1
            val dateAdministrated = vaccRec?.dateAdministrated ?: java.sql.Date(0)
            val nextDoseDate = vaccRec?.nextDoseDate ?: java.sql.Date(0)

            val vaccRecModel = VaccRecModel(vaccId, vaccName, dateAdministrated, nextDoseDate)
            vaccRecModels?.add(vaccRecModel)
        }
        recyclerView?.adapter?.notifyDataSetChanged()
    }

    override fun onDeleteClick(position: Int) {
        val vaccinationRecord = vaccRecModels?.get(position)

        vaccinationRecord?.let {
            coroutineScope.launch {
                val result = it.id?.let { it1 ->
                    VaccinationRecordSuspendedFunctions.deleteVaccRec(
                        it1, applicationContext
                    )
                }
                if (result == true) {
                    vaccRecModels?.removeAt(position)
                    recyclerView?.adapter?.notifyItemRemoved(position)
                    Toast.makeText(
                        applicationContext,
                        "Vaccination record deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Failed to delete vaccination record",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}