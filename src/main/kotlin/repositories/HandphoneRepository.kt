package org.delcom.repositories

import org.delcom.dao.HandphoneDAO
import org.delcom.entities.Handphone
import org.delcom.helpers.daoToHandphoneModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.HandphoneTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import java.util.UUID

class HandphoneRepository : IHandphoneRepository {

    override suspend fun getHandphones(search: String): List<Handphone> = suspendTransaction {
        if (search.isBlank()) {
            HandphoneDAO.all()
                .orderBy(HandphoneTable.createdAt to SortOrder.DESC)
                .limit(20)
                .map(::daoToHandphoneModel)
        } else {
            val keyword = "%${search.lowercase()}%"
            HandphoneDAO
                .find { HandphoneTable.nama.lowerCase() like keyword }
                .orderBy(HandphoneTable.nama to SortOrder.ASC)
                .limit(20)
                .map(::daoToHandphoneModel)
        }
    }

    override suspend fun getHandphoneById(id: String): Handphone? = suspendTransaction {
        HandphoneDAO
            .find { HandphoneTable.id eq UUID.fromString(id) }
            .limit(1)
            .map(::daoToHandphoneModel)
            .firstOrNull()
    }

    override suspend fun getHandphoneByName(name: String): Handphone? = suspendTransaction {
        HandphoneDAO
            .find { HandphoneTable.nama eq name }
            .limit(1)
            .map(::daoToHandphoneModel)
            .firstOrNull()
    }

    override suspend fun addHandphone(handphone: Handphone): String = suspendTransaction {
        val dao = HandphoneDAO.new {
            nama = handphone.nama
            merek = handphone.merek
            harga = handphone.harga
            deskripsi = handphone.deskripsi
            spesifikasi = handphone.spesifikasi
            pathGambar = handphone.pathGambar
            createdAt = handphone.createdAt
            updatedAt = handphone.updatedAt
        }
        dao.id.value.toString()
    }

    override suspend fun updateHandphone(id: String, newHandphone: Handphone): Boolean = suspendTransaction {
        val dao = HandphoneDAO
            .find { HandphoneTable.id eq UUID.fromString(id) }
            .limit(1)
            .firstOrNull()

        if (dao != null) {
            dao.nama = newHandphone.nama
            dao.merek = newHandphone.merek
            dao.harga = newHandphone.harga
            dao.deskripsi = newHandphone.deskripsi
            dao.spesifikasi = newHandphone.spesifikasi
            dao.pathGambar = newHandphone.pathGambar
            dao.updatedAt = newHandphone.updatedAt
            true
        } else {
            false
        }
    }

    override suspend fun removeHandphone(id: String): Boolean = suspendTransaction {
        val rowsDeleted = HandphoneTable.deleteWhere {
            HandphoneTable.id eq UUID.fromString(id)
        }
        rowsDeleted == 1
    }
}