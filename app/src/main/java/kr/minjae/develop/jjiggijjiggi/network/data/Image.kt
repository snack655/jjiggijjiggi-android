package kr.minjae.develop.jjiggijjiggi.network.data

data class Image(
    val fields: List<Field>,
    val inferResult: String,
    val message: String,
    val name: String,
    val uid: String,
    val validationResult: ValidationResult
)