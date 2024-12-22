package com.grupoct.gestionalmacen.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val userId: String
)
