package com.example.lastchancedb.register_login

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lastchancedb.R
import com.example.lastchancedb.database.password.Password
import com.example.lastchancedb.database.password.PasswordSuspendedFunctions
import com.example.lastchancedb.database.user.User
import com.example.lastchancedb.database.user.UserSuspendedFunctions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    var auth = FirebaseAuth.getInstance()

    private var nameEDTV: EditText? = null
    private var emailEDTV: EditText? = null
    private var dobTV: TextView? = null
    private var passwordEDTV: EditText? = null
    private var repeatPasswordEDTV: EditText? = null
    private var registerBTN: Button? = null
    private var loginTV: TextView? = null

    private var passwordId: Int? = null
    private var passId: Int? = null
    private var selectedDate: java.sql.Date? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEDTV = findViewById(R.id.nameUEDTV)
        emailEDTV = findViewById(R.id.emailEDTV)
        dobTV = findViewById(R.id.dobTV)
        passwordEDTV = findViewById(R.id.passwordEDTV)
        repeatPasswordEDTV = findViewById(R.id.repeatPasswordEDTV)
        registerBTN = findViewById(R.id.registerBTN)
        loginTV = findViewById(R.id.loginTV)

        loginTV?.setOnClickListener {
            goToLogin()
        }

        dobTV?.setOnClickListener {
            openDialog()
        }

        registerBTN?.setOnClickListener {
            if (validRegisterInformation()) {
                register()
                coroutineScope.launch {
                    passId = insertPassword()
                    insertUser()
                }

                goToLogin()
                Toast.makeText(this, "valid register: true", Toast.LENGTH_SHORT).show()

            }


        }
    }

    private fun validRegisterInformation(): Boolean {
        if (nameEDTV?.text.toString().isBlank()) {
            nameEDTV?.error = "Please enter your name"
            return false
        } else if ((nameEDTV?.text?.length ?: 0) >= 100) {
            nameEDTV?.error = "Please provide a valid name"
            return false
        }

        if (emailEDTV?.text.toString().isBlank()) {
            emailEDTV?.error = "Please enter your email"
            return false
        }

        //checks if the email is valid or not
        val validEmail =
            android.util.Patterns.EMAIL_ADDRESS.matcher(emailEDTV?.text.toString()).matches()
        if (!validEmail) {
            emailEDTV?.error = "Please provide a valid email"
            return false
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Please choose your date of birth", Toast.LENGTH_SHORT).show()
            return false
        }

        if (passwordEDTV?.text.toString().isBlank()) {
            passwordEDTV?.error = "Password is required"
            return false
        }

        if ((passwordEDTV?.text?.length ?: 0) < 8) {
            passwordEDTV?.error = "Password must be at least 8 characters"
            return false
        }

        val uppercaseRegex = Regex("[A-Z]")
        val lowercaseRegex = Regex("[a-z]")
        val digitRegex = Regex("[0-9]")
        val specialCharacterRegex = Regex("[^A-Za-z0-9]")

        if (!uppercaseRegex.containsMatchIn(passwordEDTV?.text.toString())) {
            passwordEDTV?.error = "Password must contain at least one uppercase letter"
            return false
        }

        if (!lowercaseRegex.containsMatchIn(passwordEDTV?.text.toString())) {
            passwordEDTV?.error = "Password must contain at least one lowercase letter"
            return false
        }

        if (!digitRegex.containsMatchIn(passwordEDTV?.text.toString())) {
            passwordEDTV?.error = "Password must contain at least one digit"
            return false
        }

        if (!specialCharacterRegex.containsMatchIn(passwordEDTV?.text.toString())) {
            passwordEDTV?.error = "Password must contain at least one special character"
            return false
        }

        if (!passwordEDTV?.text.toString().equals(repeatPasswordEDTV?.text.toString())) {
            repeatPasswordEDTV?.error = "Passwords do not match"
            return false
        }

        return true
    }

    private fun register() {
        val email: String = emailEDTV?.text.toString().trim() { it <= ' ' }
        val password: String = passwordEDTV?.text.toString().trim() { it <= ' ' }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "create user with email: success")
                    registrationSuccessful()
                } else {
                    Log.w(ContentValues.TAG, "create user with email: failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun registrationSuccessful() {
        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
    }

    private fun goToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openDialog() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, this, year, month, day)
        dialog.datePicker.maxDate = currentDate.timeInMillis
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        selectedDate = java.sql.Date(calendar.timeInMillis)
        val formattedDate = formatDate(selectedDate!!) // Format selected date
        dobTV?.text = formattedDate
    }

    private fun formatDate(date: java.sql.Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }


    //first we insert password to get the password id
    private suspend fun insertPassword(): Int {
        val password: String = passwordEDTV?.text.toString().trim()

        return coroutineScope.async {
            val pass = Password(null, password)
            val passwordId = PasswordSuspendedFunctions.insertPassword(pass, this@RegisterActivity)
            Log.d("PasswordId in register activity", "$passwordId")
            passwordId
        }.await()
    }

    //then we insert user
    private suspend fun insertUser() {
        val name: String = nameEDTV?.text.toString().trim() { it <= ' ' }
        val email: String = emailEDTV?.text.toString().trim() { it <= ' ' }
        Log.d("SelectedDate", "$selectedDate")
        val user = User(name, email, selectedDate, passId)

        coroutineScope.launch {
            UserSuspendedFunctions.insertUser(user, this@RegisterActivity)
        }

    }
}