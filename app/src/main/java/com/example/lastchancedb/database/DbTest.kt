package com.example.lastchancedb.database

import com.example.lastchancedb.database.user.User
import com.example.lastchancedb.database.user.UserQueries
import com.example.lastchancedb.database.vaccination.Vaccination
import com.example.lastchancedb.database.vaccination.VaccQueries
import com.example.lastchancedb.database.vaccination_record.VaccRecQueries
import com.example.lastchancedb.database.vaccination_record.VaccinationRecord
import java.sql.Date

fun main(){
    try{
        val connection = DatabaseConnection.getConnection()
        val userQueries = UserQueries(connection)
        val vaccQueries = VaccQueries(connection)
        val vaccRecQueries = VaccRecQueries(connection)

//        println("Testing insertUser():")
//
//        val newUser= User(
//            name = "Anna",
//            email = "anna@anna.com",
//            password = "anna"
//        )
//
//        println("User insertion successful: ${userQueries.insertUser(newUser)}")

//        println("Testing insertVacc():")
//        val newVacc= Vaccination(
//            name = "Pfizer",
//            daysUntilNextDose = 30,
//            description = "Vaccination for the cholera"
//        )
//
//        println("Vaccination insertion successful: ${vaccQueries.insertVacc(newVacc)}")


//        println("Testing deleteVacc():")
//        println("Vaccination deletion successful: ${vaccQueries.deleteVacc("Pfizer")}")

//        println("Testing insertVaccRec():")
//        val newVaccRec= VaccinationRecord(
//            userEmail = "anna@anna.com",
//            vaccName = "ava",
//            dateAdministrated = Date.valueOf("2024-02-21"),
//            nextDoseDate = Date.valueOf("2024-03-21")
//        )
//        println("Vaccination record insertion successful: ${vaccRecQueries.insertVaccRec(newVaccRec)}")


//        println("Testing getVaccRec():")
//        println("Vaccination record get successful: ${vaccRecQueries.getVaccRec(1)}")

//        print("Testing deleteVaccRec():")
//        println("Vaccination record deletion successful: ${vaccRecQueries.deleteVaccRec(1)}")


        print("Testing getAllVaccRec():")
        println("Vaccination record get successful: ${vaccRecQueries.getAllVaccRec()}")


        connection.close()
    } catch (e: Exception){
        e.printStackTrace()
    }
}