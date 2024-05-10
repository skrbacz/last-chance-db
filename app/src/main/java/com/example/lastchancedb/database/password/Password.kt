package com.example.lastchancedb.database.password

/**
 * Data class representing a password entity.
 *
 * @property id The ID of the password (nullable).
 * @property password The password string.
 */
data class Password(
    val id: Int?= null,
    val password: String
)
