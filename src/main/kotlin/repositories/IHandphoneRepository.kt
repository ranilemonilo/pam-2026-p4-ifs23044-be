package org.delcom.repositories

import org.delcom.entities.Handphone

interface IHandphoneRepository {
    suspend fun getHandphones(search: String): List<Handphone>
    suspend fun getHandphoneById(id: String): Handphone?
    suspend fun getHandphoneByName(nama: String): Handphone?
    suspend fun addHandphone(handphone: Handphone): String
    suspend fun updateHandphone(id: String, handphone: Handphone): Boolean
    suspend fun removeHandphone(id: String): Boolean
}