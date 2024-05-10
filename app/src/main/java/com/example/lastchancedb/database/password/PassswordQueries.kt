package com.example.lastchancedb.database.password

import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.Connection
import java.sql.ResultSet

/**
 * Implementation of the [PasswordDAO] interface responsible for executing password-related database queries.
 *
 * @property connection The database connection used to execute queries.
 */
class PassswordQueries(private val connection: Connection) : PasswordDAO {

    /**
     * Inserts a password into the database.
     *
     * @param password The password object containing the password to be inserted.
     * @return The ID of the inserted password.
     */
    override fun insertPassword(password: Password): Int {
        val query = "{CALL insertPassword(?)}"
        val callableStatement = connection.prepareCall(query)

        val hashedPassword = password.password.let {
            BCrypt.withDefaults().hashToString(12, it.toCharArray())
        } ?: ""

        callableStatement.setString(1, hashedPassword)

        val result = callableStatement.executeUpdate()
        var passwordId = -1

        if (result > 0) {
            // After inserting the password, fetch the last inserted passwordId
            val selectLastIdQuery = "SELECT LAST_INSERT_ID() AS last_id"
            val selectStatement = connection.createStatement()
            val resultSet = selectStatement.executeQuery(selectLastIdQuery)

            if (resultSet.next()) {
                passwordId = resultSet.getInt("last_id")
                Log.d("Pass id in password queries", "$passwordId")
            }
            selectStatement.close()
        }

        callableStatement.close()

        return passwordId
    }

    /**
     * Deletes a password from the database.
     *
     * @param passwordId The ID of the password to be deleted.
     * @return True if the password is successfully deleted, false otherwise.
     */
    override fun deletePassword(passwordId: Int): Boolean {
        val query = "{CALL deleteUser(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, passwordId)

        return callableStatement.executeUpdate() > 0
    }

}