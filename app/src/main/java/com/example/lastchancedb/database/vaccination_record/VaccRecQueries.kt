package com.example.lastchancedb.database.vaccination_record

import java.sql.Connection
import java.sql.ResultSet
import java.util.Locale

/**
 * Provides methods to execute queries related to vaccination records in the database.
 *
 * @param connection The database connection object.
 */
class VaccRecQueries(private val connection: Connection): VaccRecDAO {

    /**
     * Retrieves a vaccination record from the database by its ID.
     *
     * @param id The ID of the vaccination record to retrieve.
     * @return The retrieved vaccination record, or null if not found.
     */
    override fun getVaccRec(id: Int): VaccinationRecord? {
        val query= "{CALL getVaccRec(?)}"

        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1, id)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccRec(resultSet)
        } else {
            null
        }
    }

    /**
     * Retrieves a vaccination record from the database by user email and vaccination name.
     *
     * @param email The email of the user associated with the vaccination record.
     * @param vaccName The name of the vaccination.
     * @return The retrieved vaccination record, or null if not found.
     */
    override fun getVaccRecByUserEmailVaccName(
        email: String,
        vaccName: String
    ): VaccinationRecord? {
        val query= "{CALL getVaccRecByUserEmailVaccName(?,?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setString(1, email)
        callableStatement.setString(2, vaccName)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()) {
            mapResultSetToVaccRec(resultSet)
        } else {
            null
        }
    }

    /**
     * Retrieves all vaccination records from the database.
     *
     * @return A set of all vaccination records, or null if no records found.
     */
    override fun getAllVaccRec(): Set<VaccinationRecord?>? {
        val query= "{CALL getAllVaccRec()}"

        val callableStatement= connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()

        val vaccRecs= mutableSetOf<VaccinationRecord?>()
        while (resultSet.next()) {
            vaccRecs.add(mapResultSetToVaccRec(resultSet))
        }
        return if (vaccRecs.isEmpty()) null else vaccRecs
    }

    /**
     * Retrieves all vaccination records associated with a user from the database.
     *
     * @param email The email of the user.
     * @return A set of vaccination records associated with the user, or null if no records found.
     */
    override fun getAllVaccRecByUserEmail(email: String): Set<VaccinationRecord?>? {
        val query= "{CALL getAllVaccRecByUserEmail(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setString(1, email)
        val resultSet = callableStatement.executeQuery()

        val vaccRecs= mutableSetOf<VaccinationRecord?>()
        while (resultSet.next()) {
            vaccRecs.add(mapResultSetToVaccRec(resultSet))
        }
        return if (vaccRecs.isEmpty()) null else vaccRecs
    }

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vaccRec The vaccination record to be inserted.
     * @return True if the insertion was successful, false otherwise.
     */
    override fun insertVaccRec(vaccRec: VaccinationRecord): Boolean {
        val query= "{CALL insertVaccRec(?,?,?,?)}"
        val callableStatement= connection.prepareCall(query)

        val lowecaseVaccName = vaccRec.vaccName?.lowercase(Locale.ROOT)

        callableStatement.setString(1, vaccRec.userEmail)
        callableStatement.setString(2, lowecaseVaccName)
        callableStatement.setDate(3, vaccRec.dateAdministrated)
        callableStatement.setDate(4, vaccRec.nextDoseDate)

        val result = !callableStatement.execute()
        callableStatement.close()
        return result
    }


    /**
     * Deletes a vaccination record from the database.
     *
     * @param id The ID of the vaccination record to be deleted.
     * @return True if the deletion was successful, false otherwise.
     */
    override fun deleteVaccRec(id: Int): Boolean {
        val query= "{CALL deleteVaccRec(?)}"

        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1, id)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Updates an existing vaccination record in the database.
     *
     * @param id The ID of the vaccination record to be updated.
     * @param vaccRec The updated vaccination record.
     * @return True if the update was successful, false otherwise.
     */
    override fun updateVaccRec(id: Int, vaccRec: VaccinationRecord): Boolean {
        val query = "{CALL updateVaccRec(?,?,?,?,?)}"

        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1, id)
        callableStatement.setString(2, vaccRec.userEmail)
        callableStatement.setString(3, vaccRec.vaccName)
        callableStatement.setDate(4, vaccRec.dateAdministrated)
        callableStatement.setDate(5, vaccRec.nextDoseDate)

        return callableStatement.executeUpdate() > 0
    }

    /**
     * Maps a ResultSet object to a VaccinationRecord object.
     *
     * @param resultSet The ResultSet object containing the query results.
     * @return The mapped VaccinationRecord object.
     */
    private fun mapResultSetToVaccRec(resultSet: ResultSet): VaccinationRecord {
        return VaccinationRecord(
            id= resultSet.getInt("vaccinationRecordId"),
            userEmail= resultSet.getString("userEmail"),
            vaccName= resultSet.getString("vaccName"),
            dateAdministrated= resultSet.getDate("dateAdministrated"),
            nextDoseDate= resultSet.getDate("nextDoseDueDate")
        )
    }
}