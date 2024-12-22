package com.grupoct.gestionalmacen.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.grupoct.gestionalmacen.data.Vitrine
import com.grupoct.gestionalmacen.data.Warehouse
import com.grupoct.gestionalmacen.viewmodel.WarehouseViewModel

@Composable
fun MoveProductScreen(
    token: String,
    onMove: (String, String, String, Int) -> Unit
) {
    val viewModel = WarehouseViewModel( )
    // Observar los estados del ViewModel
    val warehouses by viewModel.warehouses.collectAsState()
    val destinations by viewModel.destinations.collectAsState()
    val vitrines by viewModel.vitrines.collectAsState()

    var selectedOriginWarehouse by remember { mutableStateOf<Warehouse?>(null) }
    var selectedDestinationWarehouse by remember { mutableStateOf<Warehouse?>(null) }
    var selectedVitrine by remember { mutableStateOf<Vitrine?>(null) }

    var productCode by remember { mutableStateOf("") }
    var productDetails by remember { mutableStateOf("Sin detalles") }
    var stockAvailable by remember { mutableStateOf(0) }
    var quantityToMove by remember { mutableStateOf("") }

    // Cargar almacenes al iniciar
    LaunchedEffect(Unit) {
        viewModel.loadWarehouses()
    }

    // Actualizar destinos al cambiar el origen
    LaunchedEffect(selectedOriginWarehouse) {
        selectedOriginWarehouse?.idtienda?.let { viewModel.loadDestinations(it) }
        selectedDestinationWarehouse = null
    }

    // Actualizar vitrinas al cambiar el destino
    LaunchedEffect(selectedDestinationWarehouse) {
        selectedDestinationWarehouse?.idtienda?.let { viewModel.loadVitrines(it) }
        selectedVitrine = null
    }

    // Función para buscar el producto
    fun searchProduct() {
        selectedOriginWarehouse?.idtienda?.let { warehouseId ->
            viewModel.getProductByCodeAndWarehouseId(productCode, warehouseId)
        } ?: println("Debe seleccionar un almacén origen")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DropdownMenuBox(
            items = warehouses,
            selectedItem = selectedOriginWarehouse,
            onItemSelected = { selectedOriginWarehouse = it },
            label = "Seleccionar Almacén Origen"
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuBox(
            items = destinations,
            selectedItem = selectedDestinationWarehouse,
            onItemSelected = { selectedDestinationWarehouse = it },
            label = "Seleccionar Almacén Destino"
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuBox(
            items = vitrines,
            selectedItem = selectedVitrine,
            onItemSelected = { selectedVitrine = it },
            label = "Seleccionar Vitrina"
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = productCode,
            onValueChange = { productCode = it },
            label = { Text("Código de Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { searchProduct() },
            enabled = productCode.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar detalles del producto
        Text(text = "Detalles: $productDetails", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Stock Disponible: $stockAvailable", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = quantityToMove,
            onValueChange = { quantityToMove = it },
            label = { Text("Cantidad a Mover") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        /*
        Button(
            onClick = {
                val quantity = quantityToMove.toIntOrNull() ?: 0
                if (quantity > 0 && quantity <= stockAvailable) {
                    onMove(
                        selectedOriginWarehouse?.name.orEmpty(),
                        selectedDestinationWarehouse?.name.orEmpty(),
                        productCode,
                        quantity
                    )
                }
            },
            enabled = selectedOriginWarehouse != null &&
                    selectedDestinationWarehouse != null &&
                    selectedVitrine != null &&
                    productCode.isNotBlank() &&
                    quantityToMove.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mover Producto")
        } */
    }
}

@Composable
fun <T> DropdownMenuBox(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    label: String
) where T : Displayable {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedItem?.displayName ?: label)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(item.displayName)
                }
            }
        }
    }
}
interface Displayable {
    val displayName: String
}
