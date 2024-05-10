package com.example.lastchancedb.database.user

/**
 * Data class representing a user entity.
 *
 * @property name The name of the user.
 * @property email The email of the user.
 * @property dob The date of birth of the user.
 * @property passwordId The ID of the password associated with the user.
 */
data class User(
    var name: String?= null,
    var email: String?= null,
    var dob: java.sql.Date?= null,
    val passwordId: Int?= null
)
