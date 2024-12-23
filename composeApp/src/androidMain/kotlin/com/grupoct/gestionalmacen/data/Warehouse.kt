package com.grupoct.gestionalmacen.data

import kotlinx.serialization.Serializable

@Serializable
data class Warehouse(
    val idtienda : Int,
    val nombreTienda : String
)