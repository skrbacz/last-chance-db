package com.example.lastchancedb.recycler_view_activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lastchancedb.MainActivity
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import com.example.lastchancedb.database.vaccination_record.VaccinationRecordSuspendedFunctions
import com.example.lastchancedb.recycler_view_activities.adapters.UserVaccRecStorageAdapter
import com.example.lastchancedb.recycler_view_activities.models.VaccRecModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserVaccRecStorageActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var vaccinationRecords: Set<VaccinationRecord?>? = null
    private var vaccRecModels: ArrayList<VaccRecModel>? = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var userEmail= "test@test.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_users_history)

        val bottomNavigationView= findViewById<LinearLayout>(R.id.bottom_navigation)

        val buttonHome= bottomNavigationView.findViewById<ImageView>(R.id.button_home)
        val buttonLibrary= bottomNavigationView.findViewById<ImageView>(R.id.button_library)
        val buttonStorage= bottomNavigationView.findViewById<ImageView>(R.id.button_storage)
        val clicked = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_pink))


        buttonStorage.imageTintList= clicked

        buttonHome.setOnClickListener {
            val intent= Intent(this@UserVaccRecStorageActivity, MainActivity::class.java)
            startActivity(intent)
        }

        buttonLibrary.setOnClickListener {
            val intent= Intent(this@UserVaccRecStorageActivity, VaccinationLibraryActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById<RecyclerView>(R.id.vaccHistoryRV)
        coroutineScope.launch {
            vaccinationRecords = VaccinationRecordSuspendedFunctions.getAllVaccRecByUserEmail(userEmail)
            setUpVaccRecModel(vaccinationRecords)
            vaccRecModels?.let {
                recyclerView?.adapter =
                    UserVaccRecStorageAdapter(this@UserVaccRecStorageActivity, it)
                recyclerView?.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this@UserVaccRecStorageActivity)
            }
        }
    }

    private fun setUpVaccRecModel(vaccinationRecords: Set<VaccinationRecord?>?) {
        for (vaccRec in vaccinationRecords!!) {
            Log.d("vacc rec from db", "${vaccRec?.vaccName}, ${vaccRec?.dateAdministrated}, ${vaccRec?.nextDoseDate}")

            vaccRec?.vaccName?.let { vaccRec.dateAdministrated.let { it1 ->
                vaccRec.nextDoseDate?.let { it2 ->
                    VaccRecModel(it,
                        it1, it2
                    )
                }
            } }
                ?.let { vaccRecModels?.add(it) }
        }
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}