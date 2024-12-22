package com.grupoct.gestionalmacen.data

import com.grupoct.gestionalmacen.ui.login.Displayable
import kotlinx.serialization.Serializable

@Serializable
data class Warehouse(
    val idtienda : Int,
    val nombreTienda : String
): Displayable {
    override val displayName: String
        get() = nombreTienda
}