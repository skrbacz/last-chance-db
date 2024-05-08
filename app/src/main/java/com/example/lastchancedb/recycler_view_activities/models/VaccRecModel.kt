package com.example.lastchancedb.recycler_view_activities.models

import java.sql.Date

class VaccRecModel(vaccName: String, dateAdministrated: Date, nextDoseDueDate: Date) {

    var vaccName: String? = vaccName
    var dateAdministrated: Date? = dateAdministrated
    var nextDoseDueDate: Date? = nextDoseDueDate


//    fun getVaccName(): String? {
//        return vaccName
//    }
//
//    fun getDateAdministrated(): Date? {
//        return dateAdministrated
//    }
//
//    fun getNextDoseDueDate(): Date? {
//        return nextDoseDueDate
//    }
}