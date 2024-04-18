package com.example.lastchancedb.database.vaccination_record

import java.sql.Date

data class VaccinationRecord(
    val id: Int?= null,
    val userEmail: String?= null,
    val vaccName: String?= null,
    val dateAdministrated: Date,
    val nextDoseDate: Date
)