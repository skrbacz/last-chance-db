package com.example.lastchancedb.database.user

import at.favre.lib.crypto.bcrypt.BCrypt
import java.sql.Connection
import java.sql.ResultSet

class UserQueries(private val connection: Connection): UserDAO{
    override fun getUser(email: String): User? {
       val query= "{CALL getUser(?)}"
       val callableStatement= connection.prepareCall(query)
       callableStatement.setString(1,email)
       val resultSet = callableStatement.executeQuery()

       return if (resultSet.next()){
           mapResultSetToUser(resultSet)
       }else{
           null
       }
    }

    override fun getAllUsers(): Set<User?>? {
        val query= "{CALL getAllUsers()}"
        val callableStatemnt= connection.prepareCall(query)
        val resultSet= callableStatemnt.executeQuery()

        val users= mutableSetOf<User?>()
        while (resultSet.next()){
            users.add(mapResultSetToUser(resultSet))
        }

        return if (users.isEmpty()) null else users
    }

    override fun insertUser(user: User): Boolean {
       val query = "{CALL insertUser(?,?,?)}"
       val callableStatement= connection.prepareCall(query)

       callableStatement.setString(1, user.name)
       callableStatement.setString(2,user.email)

        val hashedPassword = user.password?.let {
            BCrypt.withDefaults().hashToString(12, it.toCharArray())
        } ?: ""

        callableStatement.setString(3, hashedPassword)

        val result = !callableStatement.execute()

        callableStatement.close()

        return result
    }

    override fun deleteUser(email: String): Boolean {
        val query= "{CALL deleteUser(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setString(1,email)

        return callableStatement.executeUpdate() > 0
    }

    override fun updateUser(email: String, user: User): Boolean {
        val query = "{CALL updateUser(?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, user.name)
        callableStatement.setString(2, user.email)
        callableStatement.setString(3, user.password)

        return callableStatement.executeUpdate() > 0
    }

    private fun mapResultSetToUser(resultSet: ResultSet): User? {
        return User(
            name= resultSet.getString("name"),
            email= resultSet.getString("email")
        )
    }

}