package com.example.lastchancedb.database.user

interface UserDAO {

    fun getUser(email: String): User?
    fun getAllUsers(): Set<User?>?
    fun insertUser(user: User): Boolean
    fun deleteUser(email: String): Boolean
    fun updateUser(email: String, user: User): Boolean
}