package com.grupoct.gestionalmacen

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform