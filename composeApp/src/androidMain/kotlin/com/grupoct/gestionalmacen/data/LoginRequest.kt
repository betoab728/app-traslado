package com.grupoct.gestionalmacen.data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    @SerialName("Nombre") val nombre: String,
    @SerialName("Contrasenia") val contrasenia: String
)
