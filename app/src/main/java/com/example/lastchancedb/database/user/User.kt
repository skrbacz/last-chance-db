package com.example.lastchancedb.database.user

data class User(
    val name: String?= null,
    val email: String?= null,
    val dob: java.sql.Date?= null,
    val passwordId: Int?= null
)
