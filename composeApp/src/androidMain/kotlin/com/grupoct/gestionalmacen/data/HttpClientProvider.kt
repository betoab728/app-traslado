package com.grupoct.gestionalmacen.data

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Configuración centralizada de HttpClient
object HttpClientProvider {
    val client = HttpClient(CIO) {

        engine {
            requestTimeout =50_000 // Tiempo máximo para completar una solicitud (en milisegundos)
            endpoint {
                connectTimeout = 50_000 // Tiempo de espera al conectar (en milisegundos)
                socketTimeout = 50_000  // Tiempo de espera para leer/escribir datos
            }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
}

