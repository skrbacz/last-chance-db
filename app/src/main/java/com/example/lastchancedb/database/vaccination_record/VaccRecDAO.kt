package com.example.lastchancedb.database.vaccination_record

/**
 * Provides methods to interact with the vaccination record database table.
 */
interface VaccRecDAO {

    /**
     * Retrieves a vaccination record from the database by its ID.
     *
     * @param id The ID of the vaccination record to retrieve.
     * @return The retrieved vaccination record, or null if not found.
     */
    fun getVaccRec(id:Int): VaccinationRecord?

    /**
     * Retrieves a vaccination record from the database by user email and vaccination name.
     *
     * @param email The email of the user associated with the vaccination record.
     * @param vaccName The name of the vaccination.
     * @return The retrieved vaccination record, or null if not found.
     */
    fun getVaccRecByUserEmailVaccName(email: String, vaccName: String): VaccinationRecord?

    /**
     * Retrieves all vaccination records from the database.
     *
     * @return A set of all vaccination records, or null if no records found.
     */
    fun getAllVaccRec(): Set<VaccinationRecord?>?

    /**
     * Retrieves all vaccination records associated with a user from the database.
     *
     * @param email The email of the user.
     * @return A set of vaccination records associated with the user, or null if no records found.
     */
    fun getAllVaccRecByUserEmail(email: String): Set<VaccinationRecord?>?

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vaccRec The vaccination record to be inserted.
     * @return True if the insertion was successful, false otherwise.
     */
    fun insertVaccRec(vaccRec: VaccinationRecord): Boolean

    /**
     * Deletes a vaccination record from the database.
     *
     * @param id The ID of the vaccination record to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    fun deleteVaccRec(id:Int): Boolean

    /**
     * Updates an existing vaccination record in the database.
     *
     * @param id The ID of the vaccination record to be updated.
     * @param vaccRec The updated vaccination record.
     * @return True if the update was successful, false otherwise.
     */
    fun updateVaccRec(id:Int, vaccRec: VaccinationRecord): Boolean

}