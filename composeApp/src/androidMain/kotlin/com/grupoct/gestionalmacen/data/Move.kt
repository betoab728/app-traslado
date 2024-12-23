package com.grupoct.gestionalmacen.data

data class Move(
    val idproducto: Int,
    val idalmacenorigen: Int,
    val idalmacendestino: Int,
    val cantidad: Int,
    val vitrina: String,
    val idusuario: Int
)
