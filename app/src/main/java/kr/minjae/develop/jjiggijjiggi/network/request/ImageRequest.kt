package kr.minjae.develop.jjiggijjiggi.network.request

data class ImageRequest(
    val format: String,
    val name: String,
    val data: String? = null,
    val url: String
)
