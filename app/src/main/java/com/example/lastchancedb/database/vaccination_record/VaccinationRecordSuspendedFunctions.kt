package com.example.lastchancedb.database.vaccination_record

import android.content.Context
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides suspended functions to interact with the vaccination record database table.
 */
object VaccinationRecordSuspendedFunctions {

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vaccinationRecord The vaccination record to be inserted.
     * @param context The context used to display toast messages.
     */
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

    /**
     * Deletes a vaccination record from the database.
     *
     * @param id The ID of the vaccination record to be deleted.
     * @param context The context used to display toast messages.
     * @return True if the deletion was successful, false otherwise.
     */
    suspend fun deleteVaccRec(id: Int, context: Context): Boolean {
       return withContext(Dispatchers.IO) {
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
             result
        }
    }

    /**
     * Updates an existing vaccination record in the database.
     *
     * @param id The ID of the vaccination record to be updated.
     * @param vaccRec The updated vaccination record.
     * @param context The context used to display toast messages.
     */
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

    /**
     * Retrieves a vaccination record from the database by its ID.
     *
     * @param id The ID of the vaccination record to retrieve.
     * @return The retrieved vaccination record, or null if not found.
     */
    suspend fun getVaccRec(id: Int): VaccinationRecord? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.getVaccRec(id)
            connection.close()
            result
        }
    }

    /**
     * Retrieves a vaccination record from the database by user email and vaccination name.
     *
     * @param email The email of the user associated with the vaccination record.
     * @param vaccName The name of the vaccination.
     * @return The retrieved vaccination record, or null if not found.
     */
    suspend fun getVaccRecByUserEmailVaccName(email: String, vaccName: String): VaccinationRecord? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.getVaccRecByUserEmailVaccName(email, vaccName)
            connection.close()
            result
        }
    }

    /**
     * Retrieves all vaccination records from the database.
     *
     * @return A set of all vaccination records, or null if no records found.
     */
    suspend fun getAllVaccRec(): Set<VaccinationRecord?>? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.getAllVaccRec()
            connection.close()
            result
        }
    }

    /**
     * Retrieves all vaccination records associated with a user from the database.
     *
     * @param email The email of the user.
     * @return A set of vaccination records associated with the user, or null if no records found.
     */
    suspend fun getAllVaccRecByUserEmail(email: String): Set<VaccinationRecord?>? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccRecQueries = VaccRecQueries(connection)
            val result = vaccRecQueries.getAllVaccRecByUserEmail(email)
            connection.close()
            result
        }
    }

}