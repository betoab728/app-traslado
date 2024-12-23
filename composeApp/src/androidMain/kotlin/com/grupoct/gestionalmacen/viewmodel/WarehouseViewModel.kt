package com.grupoct.gestionalmacen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupoct.gestionalmacen.data.*
import com.grupoct.gestionalmacen.data.HttpClientProvider.client
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WarehouseViewModel : ViewModel() {

    private val warehouseService: WarehouseService by lazy { WarehouseService() }

    private val _moveResult = MutableStateFlow<Result<String>?>(null)
    val moveResult: StateFlow<Result<String>?> = _moveResult

    private val _warehouses = MutableStateFlow<List<Warehouse>>(emptyList())
    val warehouses: StateFlow<List<Warehouse>> = _warehouses

    private val _destinations = MutableStateFlow<List<Warehouse>>(emptyList())
    val destinations: StateFlow<List<Warehouse>> = _destinations

    private val _vitrines = MutableStateFlow<List<Vitrine>>(emptyList())
    val vitrines: StateFlow<List<Vitrine>> = _vitrines

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadWarehouses() {
        viewModelScope.launch {
            try {
                val response = warehouseService.getWarehouses()
                response?.let { _warehouses.value = it }
            } catch (e: Exception) {
                _error.value = "Error al cargar los almacenes: ${e.message}"
            }
        }
    }

    fun loadDestinations(originWarehouseId: Int) {
        viewModelScope.launch {
            try {
                val response = warehouseService.getDestinations(originWarehouseId)
                response?.let { _destinations.value = it }
            } catch (e: Exception) {
                _error.value = "Error al cargar destinos: ${e.message}"
            }
        }
    }

    fun loadVitrines(destinationWarehouseId: Int) {
        viewModelScope.launch {
            try {
                val response = warehouseService.getVitrines(destinationWarehouseId)
                response?.let { _vitrines.value = it }
            } catch (e: Exception) {
                _error.value = "Error al cargar vitrinas: ${e.message}"
            }
        }
    }

    fun getProductByCodeAndWarehouseId(productCode: String, warehouseId: Int) {
        viewModelScope.launch {
            try {
                val response = warehouseService.getProductByCodeAndWarehouseId(productCode, warehouseId)
                response?.let { _product.value = it }
            } catch (e: Exception) {
                _error.value = "Error al buscar el producto: ${e.message}"
            }
        }
    }

    fun moveProduct(token: String, move: Move) {
        viewModelScope.launch {
            try {
                val response = warehouseService.moveProduct(token, move)

                if (response.status.value in 200..299) {
                    _moveResult.value = Result.success("Traslado realizado con éxito")
                } else {
                    _moveResult.value = Result.failure(Exception("Error en el traslado: ${response.status}"))
                }
            } catch (e: ClientRequestException) {
                _moveResult.value = Result.failure(Exception("Solicitud incorrecta: ${e.message}"))
            } catch (e: ServerResponseException) {
                _moveResult.value = Result.failure(Exception("Error del servidor: ${e.message}"))
            } catch (e: Exception) {
                _moveResult.value = Result.failure(Exception("Error desconocido: ${e.message}"))
            }
        }
    }
}
