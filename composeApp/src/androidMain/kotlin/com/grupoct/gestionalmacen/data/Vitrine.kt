package com.grupoct.gestionalmacen.data

import kotlinx.serialization.Serializable

@Serializable
data class Vitrine(
    val idVitrina: Int,
    val nombreVitrina: String
)