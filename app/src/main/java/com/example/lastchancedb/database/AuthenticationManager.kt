package com.example.lastchancedb.database

import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.Connection

/**
 * Manages user authentication operations such as password verification.
 *
 * @param connection The database connection object.
 */
class AuthenticationManager(private val connection: Connection) {

    /**
     * Verifies the provided password for a given user email.
     *
     * @param email The email of the user.
     * @param providedPassword The password provided by the user for verification.
     * @return True if the provided password matches the hashed password in the database, false otherwise.
     */
    fun verifyUserPassword(email: String, providedPassword: String): Boolean {
        val hashedPassword = getHashedPasswordByEmail(email) ?: return false
        val result = BCrypt.verifyer().verify(providedPassword.toCharArray(), hashedPassword)
        return result.verified
    }

    /**
     * Retrieves the hashed password associated with a user email from the database.
     *
     * @param email The email of the user.
     * @return The hashed password associated with the user email, or null if not found.
     */
    private fun getHashedPasswordByEmail(email: String): String? {
        val query = "SELECT password FROM users WHERE email = ?"
        connection.prepareStatement(query).use { preparedStatement ->
            preparedStatement.setString(1, email)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return resultSet.getString("password")
            }
        }
        return null
    }
}