package com.example.lastchancedb.other_activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lastchancedb.MainActivity
import com.example.lastchancedb.R
import com.example.lastchancedb.dialogs.EditProfileDialogFragment
import com.example.lastchancedb.register_login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private var userEmail = Firebase.auth.currentUser?.email.toString()

    private var emailTV: TextView? = null
    private var nameTV: TextView? = null
    private var dobTV: TextView? = null

    private var logOut: LinearLayout?= null
    private var editProfile: LinearLayout?= null


    private var coroutineScope= CoroutineScope(Dispatchers.Main)

    override fun onResume() {
        super.onResume()
        coroutineScope.launch {
            val user = com.example.lastchancedb.database.user.UserSuspendedFunctions.getUser(userEmail)
            if (user != null) {
                emailTV?.text = user.email
                nameTV?.text = user.name
                dobTV?.text = user.dob.toString()
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val bottomNavigationView = findViewById<LinearLayout>(R.id.bottom_navigation)

        val buttonProfile= bottomNavigationView.findViewById<ImageView>(R.id.button_profile)
        val buttonHome = bottomNavigationView.findViewById<ImageView>(R.id.button_home)
        val buttonLibrary = bottomNavigationView.findViewById<ImageView>(R.id.button_library)
        val buttonStorage = bottomNavigationView.findViewById<ImageView>(R.id.button_storage)
        val clicked = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_pink))


        buttonProfile.backgroundTintList = clicked

        buttonHome.setOnClickListener {
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            startActivity(intent)
        }

        buttonLibrary.setOnClickListener {
            val intent = Intent(this@ProfileActivity, VaccinationLibraryActivity::class.java)
            startActivity(intent)
        }

        buttonStorage.setOnClickListener {
            val intent = Intent(this@ProfileActivity, UserVaccRecStorageActivity::class.java)
            startActivity(intent)
        }

        emailTV = findViewById(R.id.emailText)
        nameTV = findViewById(R.id.nameText)
        dobTV = findViewById(R.id.dobText)
        logOut = findViewById(R.id.logOutLayout)
        editProfile = findViewById(R.id.editProfileLayout)

        logOut?.setOnClickListener{
            Firebase.auth.signOut()
            goToLogin()
        }

        editProfile?.setOnClickListener{
            val showDialog= EditProfileDialogFragment()
            showDialog.show(supportFragmentManager, "EditProfile")
        }

    }

    private fun goToLogin() {
        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()}
}