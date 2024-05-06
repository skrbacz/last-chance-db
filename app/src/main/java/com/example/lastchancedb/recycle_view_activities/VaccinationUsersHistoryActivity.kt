package com.example.lastchancedb.recycle_view_activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lastchancedb.R

//TODO: recycle view of users history of vaccination i.e. if date of the next dose < today then its there
class VaccinationUsersHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_users_history)


    }
}