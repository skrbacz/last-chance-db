package com.example.lastchancedb.database.vaccination

interface VaccDAO {

    fun getVacc(name:String): Vaccination?
    fun getAllVaccs(): Set<Vaccination?>?
    fun insertVacc(vacc: Vaccination) : Boolean
    fun deleteVacc(name:String): Boolean
    fun updateVacc(name: String ,vacc: Vaccination): Boolean
}