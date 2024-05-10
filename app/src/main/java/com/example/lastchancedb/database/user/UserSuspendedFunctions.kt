package com.example.lastchancedb.database.user

import android.content.Context
import android.widget.Toast
import com.example.lastchancedb.database.DatabaseConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A collection of suspended functions for performing user-related database operations.
 */
object UserSuspendedFunctions {

    /**
     * Inserts a new user into the database.
     *
     * @param user The user object to insert.
     * @param context The context used to display toast messages.
     */
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

    /**
     * Deletes a user from the database by email.
     *
     * @param email The email of the user to delete.
     * @param context The context used to display toast messages.
     */
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


    /**
     * Updates an existing user in the database.
     *
     * @param email The email of the user to update.
     * @param user The updated user object.
     * @param context The context used to display toast messages.
     */
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

    /**
     * Retrieves a user from the database by email.
     *
     * @param email The email of the user to retrieve.
     * @return The user object if found, otherwise null.
     */
    suspend fun getUser(email: String): User? {
        return withContext(Dispatchers.IO) {
            val connection = DatabaseConnection.getConnection()
            val userQueries = UserQueries(connection)
            val result = userQueries.getUser(email)
            connection.close()
            result
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A set containing all users if found, otherwise null.
     */
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