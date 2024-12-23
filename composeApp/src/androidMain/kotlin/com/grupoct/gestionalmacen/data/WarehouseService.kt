package com.grupoct.gestionalmacen.data

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


class WarehouseService  {
    private val client = HttpClientProvider.client

    companion object {
        private const  val BASE_URL = "http://192.168.18.110:5077/api/Tienda"
        private const  val BASE_URLT = "http://192.168.18.110:5077/api/Traslado"
    }

    suspend fun getWarehouses(): List<Warehouse>? {
        return try {
            client.get("$BASE_URL") {

                //imprimir la respuesta
                Log.d("WarehouseService", "getWarehouses: ${client.get("$BASE_URL")}")

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

            val BASE_URL2 = "http://192.168.18.110:5077/api/Traslado"

            client.get("$BASE_URL2/$productCode/$warehouseId") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error fetching product: ${e.message}")
            null
        }
    }
    suspend fun moveProduct(token: String, move: Move ): HttpResponse {

        return client.post(BASE_URLT) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            setBody(move)
        }
    }

}
