package com.example.lastchancedb.database.password

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Object containing suspended functions for password operations.
 */
object PasswordSuspendedFunctions {

    /**
     * Inserts a password into the database.
     *
     * @param password The password to be inserted.
     * @param context The context used to display toast messages.
     * @return The ID of the inserted password.
     */
    suspend fun insertPassword(password: Password, context: Context): Int {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val passwordQueries = PassswordQueries(connection)
            val passwordId = passwordQueries.insertPassword(password)
            connection.close()

            withContext(Dispatchers.Main) {
                if (passwordId != -1) {
                    Toast.makeText(context, "Password inserted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Password insertion failed", Toast.LENGTH_SHORT).show()
                }
            }

            Log.d("PSF Password Id value", "$passwordId")
            passwordId
        }
    }

    /**
     * Deletes a password from the database.
     *
     * @param passwordId The ID of the password to be deleted.
     * @param context The context used to display toast messages.
     */
    suspend fun deletePassword(passwordId: Int, context: Context){
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val passwordQueries = PassswordQueries(connection)
            val result = passwordQueries.deletePassword(passwordId)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Password deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Password deletion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}