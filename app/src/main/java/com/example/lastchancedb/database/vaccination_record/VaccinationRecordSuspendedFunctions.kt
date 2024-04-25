package com.example.lastchancedb.database.vaccination_record

import android.content.Context
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object VaccinationRecordSuspendedFunctions {

    suspend fun insertVaccRec(vaccinationRecord: VaccinationRecord, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.insertVaccRec(vaccinationRecord)
            connection.close()
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Vaccination record inserted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "Vaccination record insertion failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    suspend fun deleteVaccRec(id: Int, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.deleteVaccRec(id)
            connection.close()
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Vaccination record deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "Vaccination record deletion failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    suspend fun updateVaccRec(id: Int, vaccRec: VaccinationRecord, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.updateVaccRec(id, vaccRec)
            connection.close()
            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Vaccination record updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Vaccination record update failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    suspend fun getVaccRec(id: Int): VaccinationRecord? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.getVaccRec(id)
            connection.close()
            result
        }
    }

    suspend fun getAllVaccRec(): Set<VaccinationRecord?>? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.getAllVaccRec()
            connection.close()
            result
        }
    }

}