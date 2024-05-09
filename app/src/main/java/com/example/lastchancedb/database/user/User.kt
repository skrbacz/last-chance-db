package com.example.lastchancedb.database.user

data class User(
    var name: String?= null,
    var email: String?= null,
    var dob: java.sql.Date?= null,
    val passwordId: Int?= null
)
