package com.example.lastchancedb.database.vaccination

import java.sql.Connection
import java.sql.ResultSet
import java.util.Locale

/**
 * Provides methods to interact with vaccination records in the database.
 *
 * @param connection The database connection.
 */
class VaccQueries(private val connection: Connection): VaccDAO {

    /**
     * Retrieves a vaccination record from the database based on its name.
     *
     * @param name The name of the vaccination record to retrieve.
     * @return The retrieved vaccination record, or null if not found.
     */
    override fun getVacc(name: String): Vaccination? {
      val query= "{CALL getVacc(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setString(1,name)
        val resultSet = callableStatement.executeQuery()

        return if (resultSet.next()){
            mapResultSetToVacc(resultSet)
        }else {
            null
        }
    }

    /**
     * Retrieves all vaccination records from the database.
     *
     * @return A set containing all vaccination records, or null if no records found.
     */
    override fun getAllVaccs(): Set<Vaccination?>? {
        val query= "{CALL getAllVaccs()}"
        val callableStatement= connection.prepareCall(query)
        val resultSet = callableStatement.executeQuery()

        val vaccs= mutableSetOf<Vaccination?>()
        while (resultSet.next()){
            vaccs.add(mapResultSetToVacc(resultSet))
        }
        return if (vaccs.isEmpty()) null else vaccs
    }

    /**
     * Inserts a new vaccination record into the database.
     *
     * @param vacc The vaccination record to insert.
     * @return True if the insertion was successful, false otherwise.
     */
    override fun insertVacc(vacc: Vaccination): Boolean {
        val query = "{CALL insertVacc(?,?,?)}"
        val callableStatement = connection.prepareCall(query)

        val uppercase = vacc.name?.uppercase(Locale.ROOT)

        callableStatement.setString(1, uppercase)
        vacc.daysUntilNextDose?.let { callableStatement.setInt(2, it) }
        callableStatement.setString(3, vacc.manufacturer)

        val result = !callableStatement.execute()
        callableStatement.close()

        return result

    }

    /**
     * Deletes a vaccination record from the database.
     *
     * @param name The name of the vaccination record to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    override fun deleteVacc(name: String): Boolean {
        val query= "{CALL deleteVacc(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setString(1,name)
        return callableStatement.executeUpdate() > 0
    }

    /**
     * Updates an existing vaccination record in the database.
     *
     * @param name The name of the vaccination record to update.
     * @param vacc The updated vaccination record.
     * @return True if the update was successful, false otherwise.
     */
    override fun updateVacc(name: String, vacc: Vaccination): Boolean {
        val query = "{CALL updateVacc(?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, vacc.name)
        vacc.daysUntilNextDose?.let { callableStatement.setInt(2, it) }
        callableStatement.setString(3,vacc.manufacturer)
        return callableStatement.executeUpdate() > 0
    }

    /**
     * Maps the result set obtained from the database query to a Vaccination object.
     *
     * @param resultSet The result set obtained from the database query.
     * @return A Vaccination object mapped from the result set.
     */
    private fun mapResultSetToVacc(resultSet: ResultSet): Vaccination? {
        return Vaccination(
            name= resultSet.getString("name"),
            daysUntilNextDose = resultSet.getInt("daysUntilNextDose"),
            manufacturer = resultSet.getString("manufacturer"),
        )
    }
}