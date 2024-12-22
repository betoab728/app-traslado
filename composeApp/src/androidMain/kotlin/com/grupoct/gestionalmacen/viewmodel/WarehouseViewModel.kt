package com.grupoct.gestionalmacen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupoct.gestionalmacen.data.Product
import com.grupoct.gestionalmacen.data.Vitrine
import com.grupoct.gestionalmacen.data.Warehouse
import com.grupoct.gestionalmacen.data.WarehouseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WarehouseViewModel(private val warehouseService: WarehouseService) : ViewModel() {

    private val _warehouses = MutableStateFlow<List<Warehouse>>(emptyList())
    val warehouses: StateFlow<List<Warehouse>> = _warehouses

    private val _destinations = MutableStateFlow<List<Warehouse>>(emptyList())
    val destinations: StateFlow<List<Warehouse>> = _destinations

    private val _vitrines = MutableStateFlow<List<Vitrine>>(emptyList())
    val vitrines: StateFlow<List<Vitrine>> = _vitrines

    private val _product = MutableStateFlow<Product?>(null)

    fun loadWarehouses() {
        viewModelScope.launch {
            val response = warehouseService.getWarehouses()
            response?.let { _warehouses.value = it }
        }
    }

    fun loadDestinations(originWarehouseId: Int) {
        viewModelScope.launch {
            val response = warehouseService.getDestinations(originWarehouseId)
            response?.let { _destinations.value = it }
        }
    }

    fun loadVitrines(destinationWarehouseId: Int) {
        viewModelScope.launch {
            val response = warehouseService.getVitrines(destinationWarehouseId)
            response?.let { _vitrines.value = it }
        }
    }

    //para buscar un producto por su código e id de tienda
    fun getProductByCodeAndWarehouseId(productCode: String, warehouseId: Int) {
        viewModelScope.launch {
            val response = warehouseService.getProductByCodeAndWarehouseId(productCode, warehouseId)
            response?.let { _product.value = it }
        }
    }
}