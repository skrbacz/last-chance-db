package com.example.lastchancedb.database

import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.Connection

class AuthenticationManager(private val connection: Connection) {

    fun verifyUserPassword(email: String, providedPassword: String): Boolean {
        val hashedPassword = getHashedPasswordByEmail(email) ?: return false
        val result = BCrypt.verifyer().verify(providedPassword.toCharArray(), hashedPassword)
        return result.verified
    }

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