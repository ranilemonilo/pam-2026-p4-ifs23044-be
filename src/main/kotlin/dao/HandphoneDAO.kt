package org.delcom.dao

import org.delcom.tables.HandphoneTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class HandphoneDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, HandphoneDAO>(HandphoneTable)

    var nama by HandphoneTable.nama
    var merek by HandphoneTable.merek
    var harga by HandphoneTable.harga
    var deskripsi by HandphoneTable.deskripsi
    var spesifikasi by HandphoneTable.spesifikasi
    var pathGambar by HandphoneTable.pathGambar
    var createdAt by HandphoneTable.createdAt
    var updatedAt by HandphoneTable.updatedAt
}