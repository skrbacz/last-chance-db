package com.example.lastchancedb.database.vaccination

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A collection of suspended functions for interacting with vaccination records in the database asynchronously.
 */
object VaccinationSuspendedFunctions {

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vaccination The vaccination record to insert.
     * @param context The context used for displaying toast messages.
     */
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

    /**
     * Deletes a vaccination record from the database.
     *
     * @param name The name of the vaccination record to delete.
     * @param context The context used for displaying toast messages.
     */
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

    /**
     * Updates an existing vaccination record in the database.
     *
     * @param name The name of the vaccination record to update.
     * @param vaccination The updated vaccination record.
     * @param context The context used for displaying toast messages.
     */
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

    /**
     * Retrieves a vaccination record from the database based on its name.
     *
     * @param name The name of the vaccination record to retrieve.
     * @return The retrieved vaccination record, or null if not found.
     */
    suspend fun getVacc(name: String): Vaccination? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val vaccQueries = VaccQueries(connection)
            val result = vaccQueries.getVacc(name)
            connection.close()
            result
        }
    }

    /**
     * Retrieves all vaccination records from the database.
     *
     * @return A set containing all vaccination records, or null if no records found.
     */
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