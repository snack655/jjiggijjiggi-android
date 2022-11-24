package kr.minjae.develop.jjiggijjiggi.network.repository.ocr

import kr.minjae.develop.jjiggijjiggi.network.request.OcrRequest
import kr.minjae.develop.jjiggijjiggi.network.response.ocr.OcrResponse

interface OcrRepository {

    suspend fun getTextInImage(ocrRequest: OcrRequest): Result<OcrResponse>

}