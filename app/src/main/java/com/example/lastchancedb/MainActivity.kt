package com.example.lastchancedb

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lastchancedb.database.DatabaseConnection
import com.example.lastchancedb.database.user.UserQueries
import com.example.lastchancedb.database.vaccination.VaccQueries
import com.example.lastchancedb.database.vaccination.Vaccination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var executeBTN: Button? = null
    private var vaccNameED: EditText?= null
    private var daysUntilNextDoseED: EditText?=null
    private var descriptionED: EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        executeBTN = findViewById(R.id.executeBTN)
        vaccNameED = findViewById(R.id.vaccNameED)
        daysUntilNextDoseED = findViewById(R.id.daysUntilNextDoseED)
        descriptionED = findViewById(R.id.descriptionED)

        executeBTN?.setOnClickListener {

            val vaccName = vaccNameED?.text.toString()
            val daysUntilNextDose = daysUntilNextDoseED?.text.toString().toInt()
            val description = descriptionED?.text.toString()
            val vacc= Vaccination(vaccName,daysUntilNextDose= 10,description)

            runBlocking { launch(Dispatchers.IO){
                insertVacc(vacc)
            } }
        }

    }

    private suspend fun insertVacc(vaccination: Vaccination) {
        withContext(Dispatchers.IO){
            val connection= DatabaseConnection.getConnection()
            val userQueries = VaccQueries(connection)
            val result = userQueries.insertVacc(vaccination)
            connection.close()

            withContext(Dispatchers.Main){
                if (result) {
                    Toast.makeText(this@MainActivity,"Vaccination inserted",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity,"Vaccination insertion failed",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}