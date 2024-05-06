package com.example.lastchancedb.database.password

interface PasswordDAO {

    fun getPassword(passwordId: Int): Password?
    fun getAllPasswords(): Set<Password?>?
    fun insertPassword(password: Password): Int
    fun deletePassword(passwordId: Int): Boolean
    fun updatePassword(passwordId: Int, password: Password): Boolean
}