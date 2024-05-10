package com.example.lastchancedb.database.password

/**
 * Interface defining operations for password data access.
 */
interface PasswordDAO {

    /**
     * Inserts a password into the database.
     *
     * @param password The password to be inserted.
     * @return The ID of the inserted password.
     */
    fun insertPassword(password: Password): Int

    /**
     * Deletes a password from the database.
     *
     * @param passwordId The ID of the password to be deleted.
     * @return True if the password was successfully deleted, false otherwise.
     */
    fun deletePassword(passwordId: Int): Boolean
}