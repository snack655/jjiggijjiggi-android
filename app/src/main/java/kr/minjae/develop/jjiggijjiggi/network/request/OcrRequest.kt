package kr.minjae.develop.jjiggijjiggi.network.request

import com.google.gson.annotations.SerializedName

data class OcrRequest(
    @SerializedName("images") val images: List<ImageRequest>,
    @SerializedName("lang") val language: String = "ko",
    @SerializedName("requestId") val requestId: String = "string",
    @SerializedName("resultType") val resultType: String = "string",
    @SerializedName("timestamp") val timestamp: Long = System.currentTimeMillis()/1000,
    @SerializedName("version") val version: String = "V1",
)