package com.example.lastchancedb.database.user

/**
 * Interface defining operations for user data access.
 */
interface UserDAO {

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return The user with the specified email, or null if not found.
     */
    fun getUser(email: String): User?

    /**
     * Retrieves all users.
     *
     * @return A set containing all users, or null if no users found.
     */
    fun getAllUsers(): Set<User?>?

    /**
     * Inserts a new user.
     *
     * @param user The user to insert.
     * @return True if the user was successfully inserted, false otherwise.
     */
    fun insertUser(user: User): Boolean

    /**
     * Deletes a user by their email.
     *
     * @param email The email of the user to delete.
     * @return True if the user was successfully deleted, false otherwise.
     */
    fun deleteUser(email: String): Boolean

    /**
     * Updates an existing user.
     *
     * @param email The email of the user to update.
     * @param user The updated user object.
     * @return True if the user was successfully updated, false otherwise.
     */
    fun updateUser(email: String, user: User): Boolean
}