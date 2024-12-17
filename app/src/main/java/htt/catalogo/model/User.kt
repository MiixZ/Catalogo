package htt.catalogo.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val pass: String,
    val role: String
)
