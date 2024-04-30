package com.example.lastchancedb.database.vaccination

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object VaccinationSuspendedFunctions {
    suspend fun insertVacc(vaccination: Vaccination, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccQueries = VaccQueries(connection)
            val result = vaccQueries.insertVacc(vaccination)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Vaccination inserted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        context, "Vaccination insertion failed", Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    suspend fun deleteVacc(name: String, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccQueries = VaccQueries(connection)
            val result = vaccQueries.deleteVacc(name)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Vaccination deleted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Vaccination deletion failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    suspend fun updateVacc(name: String, vaccination: Vaccination, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccQueries = VaccQueries(connection)
            val result = vaccQueries.updateVacc(name, vaccination)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Vaccination updated", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Vaccination update failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    suspend fun getVacc(name: String): Vaccination? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccQueries = VaccQueries(connection)
            val result = vaccQueries.getVacc(name)
            connection.close()
            result
        }
    }

    suspend fun getAllVaccs(): Set<Vaccination?>? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccQueries = VaccQueries(connection)
            val result = vaccQueries.getAllVaccs()
            Log.d("VaccinationSuspendFunctions","getAllVaccs: ${result?.first()?.name}")
            connection.close()
            result
        }
    }
}