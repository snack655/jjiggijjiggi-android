package kr.minjae.develop.jjiggijjiggi.network.repository.ocr

import javax.inject.Inject
import kr.minjae.develop.jjiggijjiggi.network.request.OcrRequest
import kr.minjae.develop.jjiggijjiggi.network.response.ocr.OcrResponse
import kr.minjae.develop.jjiggijjiggi.network.service.OcrService

class OcrRepositoryImpl @Inject constructor(
    private val service: OcrService
) : OcrRepository {

    override suspend fun getTextInImage(ocrRequest: OcrRequest): Result<OcrResponse> = kotlin.runCatching {
        service.getTextInImage(ocrRequest = ocrRequest)
    }

}

