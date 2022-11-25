package kr.minjae.develop.jjiggijjiggi.feature.camera.state

data class UploadPhotoState(
    val url: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)