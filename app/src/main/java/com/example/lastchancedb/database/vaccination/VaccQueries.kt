package com.example.lastchancedb.database.vaccination

import java.sql.Connection
import java.sql.ResultSet
import java.util.Locale

class VaccQueries(private val connection: Connection): VaccDAO {
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

    override fun insertVacc(vacc: Vaccination): Boolean {
        val query = "{CALL insertVacc(?,?,?)}"
        val callableStatement = connection.prepareCall(query)

        val lowercaseName = vacc.name?.lowercase(Locale.ROOT)

        callableStatement.setString(1, lowercaseName)
//        callableStatement.setString(2,vacc.producer)
        vacc.daysUntilNextDose?.let { callableStatement.setInt(2, it) }
        callableStatement.setString(3, vacc.description)

        val result = !callableStatement.execute()
        callableStatement.close()

        return result

    }

    override fun deleteVacc(name: String): Boolean {
        val query= "{CALL deleteVacc(?)}"
        val callableStatement= connection.prepareCall(query)
        callableStatement.setString(1,name)
        return callableStatement.executeUpdate() > 0
    }

    override fun updateVacc(name: String, vacc: Vaccination): Boolean {
        val query = "{CALL updateVacc(?,?,?)}"
        val callableStatement = connection.prepareCall(query)
        callableStatement.setString(1, vacc.name)
//        callableStatement.setString(2,vacc.producer)
        vacc.daysUntilNextDose?.let { callableStatement.setInt(2, it) }
        callableStatement.setString(3,vacc.description)
        return callableStatement.executeUpdate() > 0
    }

    private fun mapResultSetToVacc(resultSet: ResultSet): Vaccination? {
        return Vaccination(
            name= resultSet.getString("name"),
//            producer= resultSet.getString("producer"),
            daysUntilNextDose = resultSet.getInt("daysUntilNextDose"),
            description = resultSet.getString("description")
        )
    }
}