package org.delcom.services

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.delcom.data.AppException
import org.delcom.data.DataResponse
import org.delcom.data.HandphoneRequest
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.IHandphoneRepository
import java.io.File
import java.util.UUID

class HandphoneService(private val handphoneRepository: IHandphoneRepository) {

    suspend fun getAllHandphones(call: ApplicationCall) {
        val search = call.request.queryParameters["search"] ?: ""
        val handphones = handphoneRepository.getHandphones(search)
        val response = DataResponse(
            "success",
            "Berhasil mengambil daftar handphone",
            mapOf(Pair("handphones", handphones))
        )
        call.respond(response)
    }

    suspend fun getHandphoneById(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID handphone tidak boleh kosong!")
        val handphone = handphoneRepository.getHandphoneById(id)
            ?: throw AppException(404, "Data handphone tidak tersedia!")
        val response = DataResponse(
            "success",
            "Berhasil mengambil data handphone",
            mapOf(Pair("handphone", handphone))
        )
        call.respond(response)
    }

    private suspend fun getHandphoneRequest(call: ApplicationCall): HandphoneRequest {
        val req = HandphoneRequest()
        val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 5)
        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "nama" -> req.nama = part.value.trim()
                        "merek" -> req.merek = part.value.trim()
                        "harga" -> req.harga = part.value.trim().toLongOrNull() ?: 0L
                        "deskripsi" -> req.deskripsi = part.value
                        "spesifikasi" -> req.spesifikasi = part.value
                    }
                }
                is PartData.FileItem -> {
                    val ext = part.originalFileName
                        ?.substringAfterLast('.', "")
                        ?.let { if (it.isNotEmpty()) ".$it" else "" } ?: ""
                    val fileName = UUID.randomUUID().toString() + ext
                    val filePath = "uploads/handphones/$fileName"
                    val file = File(filePath)
                    file.parentFile.mkdirs()
                    part.provider().copyAndClose(file.writeChannel())
                    req.pathGambar = filePath
                }
                else -> {}
            }
            part.dispose()
        }
        return req
    }

    private fun validateHandphoneRequest(req: HandphoneRequest) {
        val validator = ValidatorHelper(req.toMap())
        validator.required("nama", "Nama tidak boleh kosong")
        validator.required("merek", "Merek tidak boleh kosong")
        validator.required("deskripsi", "Deskripsi tidak boleh kosong")
        validator.required("spesifikasi", "Spesifikasi tidak boleh kosong")
        validator.required("pathGambar", "Gambar tidak boleh kosong")
        validator.validate()

        val file = File(req.pathGambar)
        if (!file.exists()) throw AppException(400, "Gambar handphone gagal diupload!")
    }

    suspend fun createHandphone(call: ApplicationCall) {
        val req = getHandphoneRequest(call)
        validateHandphoneRequest(req)

        val exist = handphoneRepository.getHandphoneByName(req.nama)
        if (exist != null) {
            File(req.pathGambar).takeIf { it.exists() }?.delete()
            throw AppException(409, "Handphone dengan nama ini sudah terdaftar!")
        }

        val id = handphoneRepository.addHandphone(req.toEntity())
        call.respond(DataResponse("success", "Berhasil menambahkan data handphone", mapOf("handphoneId" to id)))
    }

    suspend fun updateHandphone(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID handphone tidak boleh kosong!")
        val old = handphoneRepository.getHandphoneById(id)
            ?: throw AppException(404, "Data handphone tidak tersedia!")

        val req = getHandphoneRequest(call)
        if (req.pathGambar.isEmpty()) req.pathGambar = old.pathGambar
        validateHandphoneRequest(req)

        if (req.nama != old.nama) {
            val exist = handphoneRepository.getHandphoneByName(req.nama)
            if (exist != null) {
                File(req.pathGambar).takeIf { it.exists() }?.delete()
                throw AppException(409, "Handphone dengan nama ini sudah terdaftar!")
            }
        }

        if (req.pathGambar != old.pathGambar) {
            File(old.pathGambar).takeIf { it.exists() }?.delete()
        }

        val updated = handphoneRepository.updateHandphone(id, req.toEntity())
        if (!updated) throw AppException(400, "Gagal memperbarui data handphone!")

        call.respond(DataResponse("success", "Berhasil mengubah data handphone", null))
    }

    suspend fun deleteHandphone(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID handphone tidak boleh kosong!")
        val old = handphoneRepository.getHandphoneById(id)
            ?: throw AppException(404, "Data handphone tidak tersedia!")

        val deleted = handphoneRepository.removeHandphone(id)
        if (!deleted) throw AppException(400, "Gagal menghapus data handphone!")

        File(old.pathGambar).takeIf { it.exists() }?.delete()
        call.respond(DataResponse("success", "Berhasil menghapus data handphone", null))
    }

    suspend fun getHandphoneImage(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: return call.respond(HttpStatusCode.BadRequest)
        val handphone = handphoneRepository.getHandphoneById(id)
            ?: return call.respond(HttpStatusCode.NotFound)
        val file = File(handphone.pathGambar)
        if (!file.exists()) return call.respond(HttpStatusCode.NotFound)
        call.respondFile(file)
    }
}