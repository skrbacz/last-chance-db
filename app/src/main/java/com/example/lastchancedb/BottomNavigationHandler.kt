package com.example.lastchancedb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.lastchancedb.recycler_view_activities.UserVaccRecStorageActivity
import com.example.lastchancedb.recycler_view_activities.VaccinationLibraryActivity

class BottomNavigationHandler(private val context: Context) {

    private var lastClickedView: ImageView?= null
    fun setupNavigation(
        homeView: ImageView,
        storageView: ImageView,
        libraryView: ImageView
    ) {

        homeView.setOnClickListener {
            switchActivity(homeView,MainActivity::class.java)
        }
        storageView.setOnClickListener{
            switchActivity(storageView,UserVaccRecStorageActivity::class.java)
        }
        libraryView.setOnClickListener{
            switchActivity(libraryView,VaccinationLibraryActivity::class.java)
        }
    }

    private fun switchActivity(clickedView: ImageView,destination: Class<*>){

        lastClickedView?.setColorFilter(ContextCompat.getColor(context,R.color.light_pink))
        clickedView.setColorFilter(ContextCompat.getColor(context, R.color.main_pink))
        lastClickedView = clickedView

        context.startActivity(Intent(context, destination))
        (context as? Activity)?.finish()
    }
}