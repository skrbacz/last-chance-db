package com.example.lastchancedb.database.vaccination

data class Vaccination(
    val name:String?=null,
    val daysUntilNextDose: Int?= null,
    val description:String?=null
)
