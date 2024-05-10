package com.example.lastchancedb.database.vaccination

/**
 * Represents a vaccination record with details such as name, days until next dose, and manufacturer.
 *
 * @property name The name of the vaccination.
 * @property daysUntilNextDose The number of days until the next dose of the vaccination.
 * @property manufacturer The manufacturer of the vaccination.
 */
data class Vaccination(
    val name:String?=null,
    val daysUntilNextDose: Int?= null,
    val manufacturer:String?=null
)
