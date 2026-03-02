package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.PlantDAO
import org.delcom.dao.HandphoneDAO
import org.delcom.entities.Plant
import org.delcom.entities.Handphone
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: PlantDAO) = Plant(
    dao.id.value.toString(),
    dao.nama,
    dao.pathGambar,
    dao.deskripsi,
    dao.manfaat,
    dao.efekSamping,
    dao.createdAt,
    dao.updatedAt
)

fun daoToHandphoneModel(dao: HandphoneDAO) = Handphone(
    dao.id.value.toString(),
    dao.nama,
    dao.merek,
    dao.harga,
    dao.deskripsi,
    dao.spesifikasi,
    dao.pathGambar,
    dao.createdAt,
    dao.updatedAt
)