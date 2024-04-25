package com.example.lastchancedb.database.user

import android.content.Context
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserSuspendedFunctions {

    suspend fun insertUser(user: User, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val userQueries = UserQueries(connection)
            val result = userQueries.insertUser(user)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "User inserted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "User insertion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun deleteUser(email: String, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val userQueries = UserQueries(connection)
            val result = userQueries.deleteUser(email)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "User deletion failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    suspend fun updateUser(email: String, user: User, context: Context) {
        withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val userQueries = UserQueries(connection)
            val result = userQueries.updateUser(email, user)
            connection.close()

            withContext(Dispatchers.Main) {
                if (result) {
                    Toast.makeText(context, "User updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "User update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun getUser(email: String): User? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val userQueries = UserQueries(connection)
            val result = userQueries.getUser(email)
            connection.close()
            result
        }
    }

    suspend fun getAllUsers(): Set<User?>? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val userQueries = UserQueries(connection)
            val result = userQueries.getAllUsers()
            connection.close()

            result
        }
    }
}