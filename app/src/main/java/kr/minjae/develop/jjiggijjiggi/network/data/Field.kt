package kr.minjae.develop.jjiggijjiggi.network.data

data class Field(
    val boundingPoly: BoundingPoly,
    val inferConfidence: Double,
    val inferText: String,
    val valueType: String
)