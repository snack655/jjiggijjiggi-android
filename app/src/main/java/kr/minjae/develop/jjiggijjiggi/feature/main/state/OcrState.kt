package kr.minjae.develop.jjiggijjiggi.feature.main.state

import kr.minjae.develop.jjiggijjiggi.network.response.ocr.OcrResponse

data class OcrState(
    val isLoading: Boolean = false,
    val ocrData: OcrResponse? = null,
    val error: String = ""
)
