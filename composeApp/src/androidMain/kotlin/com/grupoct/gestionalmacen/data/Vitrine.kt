package com.grupoct.gestionalmacen.data

import com.grupoct.gestionalmacen.ui.login.Displayable
import kotlinx.serialization.Serializable

@Serializable
data class Vitrine(
    val idVitrina: Int,
    val nombreVitrina: String
) : Displayable {
    override val displayName: String
        get() = nombreVitrina
}
