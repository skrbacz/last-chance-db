package com.example.lastchancedb.other_activities.models

import java.sql.Date

class VaccRecModel(id: Int ,vaccName: String, dateAdministrated: Date?, nextDoseDueDate: Date?) {

    var id: Int? = id
    var vaccName: String? = vaccName
    var dateAdministrated: Date? = dateAdministrated
    var nextDoseDueDate: Date? = nextDoseDueDate
}