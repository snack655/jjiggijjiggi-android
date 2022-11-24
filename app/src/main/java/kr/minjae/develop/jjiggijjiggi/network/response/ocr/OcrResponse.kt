package kr.minjae.develop.jjiggijjiggi.network.response.ocr

import kr.minjae.develop.jjiggijjiggi.network.data.Image

data class OcrResponse(
    val images: List<Image>,
    val requestId: String,
    val timestamp: Long,
    val version: String
)