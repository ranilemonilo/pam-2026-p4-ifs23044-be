package org.delcom.data

import kotlinx.serialization.Serializable
import org.delcom.entities.Handphone

@Serializable
data class HandphoneRequest(
    var nama: String = "",
    var merek: String = "",
    var harga: Long = 0,
    var deskripsi: String = "",
    var spesifikasi: String = "",
    var pathGambar: String = "",
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nama" to nama,
            "merek" to merek,
            "harga" to harga,
            "deskripsi" to deskripsi,
            "spesifikasi" to spesifikasi,
            "pathGambar" to pathGambar
        )
    }

    fun toEntity(): Handphone {
        return Handphone(
            nama = nama,
            merek = merek,
            harga = harga,
            deskripsi = deskripsi,
            spesifikasi = spesifikasi,
            pathGambar = pathGambar,
        )
    }
}