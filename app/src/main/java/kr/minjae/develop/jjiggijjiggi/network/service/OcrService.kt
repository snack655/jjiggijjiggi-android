package kr.minjae.develop.jjiggijjiggi.network.service

import kr.minjae.develop.jjiggijjiggi.network.request.OcrRequest
import kr.minjae.develop.jjiggijjiggi.network.response.ocr.OcrResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OcrService {

    @POST("general")
    suspend fun getTextInImage(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("X-OCR-SECRET") xOcrSecret: String = "eGNzdmxDTHFSR3p0YUdkZ0lwT0VPdlhCQXVNTWJ0dUE=",
        @Body ocrRequest: OcrRequest
    ): OcrResponse

}