package com.example.lastchancedb.database.password

import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.Connection
import java.sql.ResultSet

class PassswordQueries(private val connection: Connection) : PasswordDAO {
    override fun getPassword(passwordId: Int): Password? {
        val query = "{CALL getPassword(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setInt(1, passwordId)
        val resultSet = callableStatement.executeQuery()
        return if (resultSet.next()) {
            mapResultSetToPassword(resultSet)
        } else {
            null
        }
    }

    override fun getAllPasswords(): Set<Password?>? {
        val query = "{CALL getAllPasswords()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()

        val passwords = mutableSetOf<Password?>()
        while (resultSet.next()) {
            passwords.add(mapResultSetToPassword(resultSet))
        }
        return if (passwords.isEmpty()) null else passwords
    }

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
            val generatedKeys = callableStatement.generatedKeys
            if (generatedKeys.next()) {
                passwordId = generatedKeys.getInt(1)
            }
        }

        callableStatement.close()

        return passwordId
    }

    override fun deletePassword(passwordId: Int): Boolean {
        val query= "{CALL deleteUser(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1, passwordId)

        return callableStatement.executeUpdate() > 0
    }

    override fun updatePassword(passwordId: Int, password: Password): Boolean {
        val query= "{CALL updatePassword(?,?)}"
        val callableStatement= connection.prepareCall(query)

        val hashedPassword = password.password.let {
            BCrypt.withDefaults().hashToString(12, it.toCharArray())
        } ?: ""

        callableStatement.setInt(1, passwordId)
        callableStatement.setString(2, hashedPassword)
        return callableStatement.executeUpdate() > 0
    }

    private fun mapResultSetToPassword(resultSet: ResultSet): Password? {
        return Password(
            id = resultSet.getInt("passwordId"),
            password = resultSet.getString("password")
        )
    }
}