package com.example.lastchancedb.database.vaccination

/**
 * An interface defining various operations for interacting with vaccination records in the database.
 */
interface VaccDAO {

    /**
     * Retrieves a vaccination record from the database by name.
     *
     * @param name The name of the vaccination record to retrieve.
     * @return The vaccination record object if found, otherwise null.
     */
    fun getVacc(name:String): Vaccination?

    /**
     * Retrieves all vaccination records from the database.
     *
     * @return A set containing all vaccination records if found, otherwise null.
     */
    fun getAllVaccs(): Set<Vaccination?>?

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vacc The vaccination record object to insert.
     * @return True if the insertion was successful, false otherwise.
     */
    fun insertVacc(vacc: Vaccination) : Boolean

    /**
     * Deletes a vaccination record from the database by name.
     *
     * @param name The name of the vaccination record to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    fun deleteVacc(name:String): Boolean

    /**
     * Updates an existing vaccination record in the database.
     *
     * @param name The name of the vaccination record to update.
     * @param vacc The updated vaccination record object.
     * @return True if the update was successful, false otherwise.
     */
    fun updateVacc(name: String ,vacc: Vaccination): Boolean
}