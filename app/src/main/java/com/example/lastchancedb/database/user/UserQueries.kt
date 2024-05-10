package com.example.lastchancedb.database.user

import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.Connection
import java.sql.ResultSet

/**
 * Class implementing user data access operations.
 *
 * @param connection The database connection to use for executing queries.
 */
class UserQueries(private val connection: Connection) : UserDAO {

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return The user with the specified email, or null if not found.
     */
    override fun getUser(email: String): User? {
        val query = "{CALL getUser(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, email)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToUser(resultSet)
        } else {
            null
        }
    }

    /**
     * Retrieves all users.
     *
     * @return A set containing all users, or null if no users found.
     */
    override fun getAllUsers(): Set<User?>? {
        val query = "{CALL getAllUsers()}"
        val callableStatement = connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()

        val users = mutableSetOf<User?>()
        while (resultSet.next()) {
            users.add(mapResultSetToUser(resultSet))
        }

        return if (users.isEmpty()) null else users
    }

    /**
     * Inserts a new user.
     *
     * @param user The user to insert.
     * @return True if the user was successfully inserted, false otherwise.
     */
    override fun insertUser(user: User): Boolean {
        val query = "{CALL insertUser(?,?,?,?)}"
        val callableStatement = connection.prepareCall(query)

        callableStatement.setString(1, user.name)
        callableStatement.setString(2, user.email)
        callableStatement.setDate(3, user.dob)
        user.passwordId?.let { callableStatement.setInt(4, it) }


        val result = !callableStatement.execute()

        callableStatement.close()

        return result
    }

    /**
     * Deletes a user by their email.
     *
     * @param email The email of the user to delete.
     * @return True if the user was successfully deleted, false otherwise.
     */
    override fun deleteUser(email: String): Boolean {
        val query = "{CALL deleteUser(?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, email)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Updates an existing user.
     *
     * @param email The email of the user to update.
     * @param user The updated user object.
     * @return True if the user was successfully updated, false otherwise.
     */
    override fun updateUser(email: String, user: User): Boolean {
        val query = "{CALL updateUser(?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, email)
        callableStatement.setString(2, user.name)
        callableStatement.setDate(3, user.dob)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Maps a result set to a User object.
     *
     * @param resultSet The result set containing user data.
     * @return The User object mapped from the result set.
     */
    private fun mapResultSetToUser(resultSet: ResultSet): User? {
        return User(
            name = resultSet.getString("name"),
            email = resultSet.getString("email"),
            dob = resultSet.getDate("dateOfBirth")
        )
    }

}