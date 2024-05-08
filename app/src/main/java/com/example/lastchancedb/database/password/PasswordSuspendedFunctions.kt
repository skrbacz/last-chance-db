package com.example.lastchancedb.database.password

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PasswordSuspendedFunctions {

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

    suspend fun updatePassword(passwordId: Int, password: Password, context: Context){
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val passwordQueries = PassswordQueries(connection)
            val result = passwordQueries.updatePassword(passwordId, password)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Password update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun getPassword(passwordId: Int): Password? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val passwordQueries = PassswordQueries(connection)
            val result = passwordQueries.getPassword(passwordId)
            connection.close()
            result
        }
    }

    suspend fun getAllPasswords(): Set<Password?>? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val passwordQueries = PassswordQueries(connection)
            val result = passwordQueries.getAllPasswords()
            connection.close()
            result
        }
    }
}