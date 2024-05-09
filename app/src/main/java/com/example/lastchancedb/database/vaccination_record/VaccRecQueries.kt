package com.example.lastchancedb.database.vaccination_record

import java.sql.Connection
import java.sql.ResultSet
import java.util.Locale

class VaccRecQueries(private val connection: Connection): VaccRecDAO {
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

    override fun deleteVaccRec(id: Int): Boolean {
        val query= "{CALL deleteVaccRec(?)}"

        val callableStatement= connection.prepareCall(query)
        callableStatement.setInt(1, id)

        return callableStatement.executeUpdate() > 0
    }

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