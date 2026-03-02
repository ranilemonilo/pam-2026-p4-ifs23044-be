package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object HandphoneTable : UUIDTable("handphones") {
    val nama = varchar("nama", 100)
    val merek = varchar("merek", 100)
    val harga = long("harga")
    val deskripsi = text("deskripsi")
    val spesifikasi = text("spesifikasi")
    val pathGambar = varchar("path_gambar", 255)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}