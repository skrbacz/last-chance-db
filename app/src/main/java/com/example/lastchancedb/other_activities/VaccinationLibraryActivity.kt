package com.example.lastchancedb.other_activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lastchancedb.MainActivity
import com.example.lastchancedb.R
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.database.vaccination.VaccinationSuspendedFunctions
import com.example.lastchancedb.other_activities.adapters.VaccLibraryAdapter
import com.example.lastchancedb.other_activities.models.VaccModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//TODO: items from nikola
class VaccinationLibraryActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var vaccinations: Set<Vaccination?>? = null
    private var vaccModels: ArrayList<VaccModel>? = ArrayList()
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_library)

        val bottomNavigationView = findViewById<LinearLayout>(R.id.bottom_navigation)

        val buttonProfile= bottomNavigationView.findViewById<ImageView>(R.id.button_profile)
        val buttonHome = bottomNavigationView.findViewById<ImageView>(R.id.button_home)
        val buttonLibrary = bottomNavigationView.findViewById<ImageView>(R.id.button_library)
        val buttonStorage = bottomNavigationView.findViewById<ImageView>(R.id.button_storage)
        val clicked = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_pink))

        buttonLibrary.imageTintList = clicked

        buttonHome.setOnClickListener {
            val intent = Intent(this@VaccinationLibraryActivity, MainActivity::class.java)
            startActivity(intent)
        }

        buttonStorage.setOnClickListener {
            val intent =
                Intent(this@VaccinationLibraryActivity, UserVaccRecStorageActivity::class.java)
            startActivity(intent)
        }

        buttonProfile.setOnClickListener {
            val intent = Intent(this@VaccinationLibraryActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById<RecyclerView>(R.id.vaccLibraryRV)
        coroutineScope.launch {
            vaccinations = VaccinationSuspendedFunctions.getAllVaccs()
            setUpVaccModel(vaccinations)
            vaccModels?.let {
                recyclerView?.adapter = VaccLibraryAdapter(this@VaccinationLibraryActivity, it)
                recyclerView?.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this@VaccinationLibraryActivity)
            }
        }
    }

    private fun setUpVaccModel(vaccs: Set<Vaccination?>?) {
        for (vacc in vaccs!!) {
            if (vacc != null) {
                vacc.name?.let {
                    vacc.manufacturer?.let { it1 ->
                        vacc.daysUntilNextDose?.let { it2 ->
                            VaccModel(
                                it, it2,
                                it1
                            )
                        }
                    }
                }
                    ?.let { vaccModels?.add(it) }
            }
        }
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}