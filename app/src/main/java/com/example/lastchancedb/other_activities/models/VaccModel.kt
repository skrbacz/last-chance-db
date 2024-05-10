package com.example.lastchancedb.other_activities.models

/**
 * Model class representing a vaccination item.
 *
 * @param name The name of the vaccination.
 * @param daysUntilNextDose The number of days until the next dose is due.
 * @param manufacturer A description of the vaccination.
 */
class VaccModel(name: String, daysUntilNextDose: Int, manufacturer: String) {

    /** The name of the vaccination. */
    var name: String? = name

    /** The number of days until the next dose is due. */
    var daysUntilNextDose: Int? = daysUntilNextDose

    /** A manufacturer of the vaccination. */
    var manufacturer: String? = manufacturer
}