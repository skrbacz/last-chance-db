package com.example.lastchancedb.database.vaccination_record

interface VaccRecDAO {

    fun getVaccRec(id:Int): VaccinationRecord?

    fun getVaccRecByUserEmailVaccName(email: String, vaccName: String): VaccinationRecord?
    fun getAllVaccRec(): Set<VaccinationRecord?>?
    fun getAllVaccRecByUserEmail(email: String): Set<VaccinationRecord?>?
    fun insertVaccRec(vaccRec: VaccinationRecord): Boolean
    fun deleteVaccRec(id:Int): Boolean
    fun updateVaccRec(id:Int, vaccRec: VaccinationRecord): Boolean

}