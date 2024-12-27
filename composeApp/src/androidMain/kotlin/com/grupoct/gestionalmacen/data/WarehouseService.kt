package com.grupoct.gestionalmacen.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.ktor.http.contentType
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.cio.Response


class WarehouseService (private val context: Context)  {
    private val client = HttpClientProvider.client

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        private const  val BASE_URL = "http://192.168.18.110:5077/api/Tienda"
        private const  val BASE_URLT = "http://192.168.18.110:5077/api/Traslado"
    }
    private fun getToken(): String? {

        val token = sharedPreferences.getString("token", null)

        Log.d("Token", "Token existente: $token")
        return token

    }

    suspend fun getWarehouses(): List<Warehouse>? {
        return try {
            client.get("$BASE_URL") {

                   val token = getToken()

                    if (token != null) {
                        header("Authorization", "Bearer $token")
                    }

                //imprimir la respuesta
               // Log.d("WarehouseService", "getWarehouses: ${client.get("$BASE_URL")}")

                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error fetching warehouses: ${e.message}")
            null
        }
    }

    suspend fun getDestinations(originWarehouseId: Int): List<Warehouse>? {
        return try {


            client.get("$BASE_URL/$originWarehouseId/almacenes-destino") {

                //token
                val token = getToken()
                if (token != null) {
                    header("Authorization", "Bearer $token")
                }

                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error fetching destinations: ${e.message}")
            null
        }
    }

    suspend fun getVitrines(destinationWarehouseId: Int): List<Vitrine>? {
        return try {

            client.get("$BASE_URL/$destinationWarehouseId/vitrinas") {

                //token
                val token = getToken()
                if (token != null) {
                    header("Authorization", "Bearer $token")
                }
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error fetching vitrines: ${e.message}")
            null
        }
    }

    //para buscar un producto por su código e id de tienda, este es el endpoint:http://localhost:5077/api/Traslado/9010WHI/1
    suspend fun getProductByCodeAndWarehouseId(productCode: String, warehouseId: Int): Product? {
        return try {


            client.get("$BASE_URLT/$productCode/$warehouseId") {
                //token
                val token = getToken()
                if (token != null) {
                    header("Authorization", "Bearer $token")
                }
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error fetching product: ${e.message}")
            null
        }
    }
    suspend fun moveProduct( move: Move ): HttpResponse {

        return client.post(BASE_URLT) {

            val token = getToken()
            if (token != null) {
                header("Authorization","Bearer $token")
            }

            contentType(ContentType.Application.Json)

            setBody(move)
        }
    }

}
