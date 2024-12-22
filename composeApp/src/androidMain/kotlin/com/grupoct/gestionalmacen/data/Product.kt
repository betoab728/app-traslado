package com.grupoct.gestionalmacen.data

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val idproducto: Int,
    val descripcion: String,
    val stock: Int
)
