package com.grupoct.gestionalmacen.data

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class AuthService {
    private val client = HttpClientProvider.client
    companion object {
        private const val BASE_URL = "http://192.168.18.110:5077/api/auth/login"
    }

    suspend fun login(Nombre: String, Contrasenia: String): String? {

        return try {

            val loginRequest = LoginRequest(Nombre, Contrasenia)
            val response: HttpResponse = client.post(BASE_URL) {
                contentType(ContentType.Application.Json)
                setBody(loginRequest) // Usa el objeto serializable
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val responseBody = response.bodyAsText()
                    val loginResponse = Json.decodeFromString<LoginResponse>(responseBody)
                    loginResponse.token
                }
                else -> {
                    println("Login failed with status: ${response.status}")
                    null
                }
            }


        } catch (e: Exception) {
            println("Error during login: ${e.message}")
            null
        }
    }

}

