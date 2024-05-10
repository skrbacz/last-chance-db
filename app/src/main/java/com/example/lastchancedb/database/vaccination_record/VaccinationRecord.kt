package com.example.lastchancedb.database.vaccination_record

import java.sql.Date

/**
 * Represents a vaccination record in the database.
 *
 * @property id The unique identifier of the vaccination record.
 * @property userEmail The email address of the user associated with the vaccination record.
 * @property vaccName The name of the vaccination.
 * @property dateAdministrated The date when the vaccination was administrated.
 * @property nextDoseDate The date of the next scheduled dose.
 */
data class VaccinationRecord(
    val id: Int?= null,
    val userEmail: String?= null,
    val vaccName: String?= null,
    var dateAdministrated: Date?= null,
    var nextDoseDate: Date?= null
)