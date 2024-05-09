package com.example.lastchancedb.database.password

interface PasswordDAO {
    fun insertPassword(password: Password): Int
    fun deletePassword(passwordId: Int): Boolean
}