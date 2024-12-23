package com.grupoct.gestionalmacen.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.grupoct.gestionalmacen.data.Move
import com.grupoct.gestionalmacen.data.Vitrine
import com.grupoct.gestionalmacen.data.Warehouse
import com.grupoct.gestionalmacen.viewmodel.WarehouseViewModel

@Composable

fun MoveProductScreen(  viewModel: WarehouseViewModel,
     token: String
)
 //genera codigo cargar almacenes origen al iniciar la pantalla



{
    LaunchedEffect(Unit) {
        viewModel.loadWarehouses()
    }

    val warehouses by viewModel.warehouses.collectAsState(initial = emptyList())
    val destinations by viewModel.destinations.collectAsState(initial = emptyList())
    val vitrines by viewModel.vitrines.collectAsState(initial = emptyList())
    val productDetails by viewModel.product.collectAsState(initial = null)

    // Estados para selección
    val selectedOrigin = remember { mutableStateOf<Warehouse?>(null) }
    val selectedDestination = remember { mutableStateOf<Warehouse?>(null) }
    val selectedVitrine = remember { mutableStateOf<Vitrine?>(null) }
    val productCode = remember { mutableStateOf("") }
    val moveQuantity = remember { mutableStateOf("") }
    val userId = 1 // Reemplazar con el ID del usuario autenticado.

    // Llamar a la función para obtener los almacenes destino al seleccionar un almacén origen
    LaunchedEffect(selectedOrigin.value) {
        selectedOrigin.value?.idtienda?.let { originWarehouseId ->
            viewModel.loadDestinations(originWarehouseId)
        }
    }

    //llamar a la funciona para listar vitrinas al seleccionar un almacen destino
    LaunchedEffect(selectedDestination.value) {
        selectedDestination.value?.idtienda?.let { destinationWarehouseId ->
            viewModel.loadVitrines(destinationWarehouseId)
        }
    }

    // Diseño de la pantalla
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // ComboBox: Almacén Origen
        DropdownMenu(
            label = "Almacén Origen",
            items = warehouses,
            selectedItem = selectedOrigin.value,
            onItemSelected = { warehouse -> selectedOrigin.value = warehouse },
            itemLabel = { it.nombreTienda }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ComboBox: Almacén Destino
        DropdownMenu(
            label = "Almacén Destino",
            items = destinations,
            selectedItem = selectedDestination.value,
            onItemSelected = { warehouse -> selectedDestination.value = warehouse },
            enabled = selectedOrigin.value != null,
            itemLabel = { it.nombreTienda }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // ComboBox: Vitrina
        // DropdownMenu: Vitrines
        DropdownMenu(
            label = "Vitrina",
            items = vitrines,
            selectedItem = selectedVitrine.value,
            onItemSelected = { vitrine -> selectedVitrine.value = vitrine },
            enabled = selectedDestination.value != null,
            itemLabel = { it.nombreVitrina }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input: Código de Producto
        OutlinedTextField(
            value = productCode.value,
            onValueChange = { productCode.value = it },
            label = { Text("Código de Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón: Buscar Producto
        Button(
            onClick = {
                selectedOrigin.value?.idtienda?.let { originWarehouseId ->
                    viewModel.getProductByCodeAndWarehouseId(productCode.value, originWarehouseId)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedOrigin.value != null && productCode.value.isNotEmpty()
        ) {
            Text("Buscar Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Mostrar descripción del producto
        // Mostrar detalles del producto y su stock , si no se ha consultado mostrar mensaje de "Producto no encontrado"

        val stockAvailable = productDetails?.stock ?: 0
        val productDescription = productDetails?.descripcion ?: "Sin descripcion"

        // Mostrar detalles del producto
        Text(text = "Detalles: $productDescription", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Stock Disponible: $stockAvailable", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))

        // Input: Cantidad
        OutlinedTextField(
            value = moveQuantity.value,
            onValueChange = { moveQuantity.value = it },
            label = { Text("Cantidad a Mover") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón: Mover Producto
        Button(
            onClick = {
                val move = Move(
                    idproducto = productDetails?.idproducto ?: 0,
                    idalmacenorigen = selectedOrigin.value?.idtienda ?: 0,
                    idalmacendestino = selectedDestination.value?.idtienda ?: 0,
                    cantidad = moveQuantity.value.toIntOrNull() ?: 0,
                    vitrina = selectedVitrine.value?.nombreVitrina ?: "",
                    idusuario = userId
                )

            //definir token



                viewModel.moveProduct("token", move)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedOrigin.value != null &&
                    selectedDestination.value != null &&
                    selectedVitrine.value != null &&
                    productDetails != null &&
                    moveQuantity.value.toIntOrNull() != null
        ) {
            Text("Hacer Traslado")
        }
    }
}

@Composable
fun <T> DropdownMenu(
    label: String,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    enabled: Boolean = true,
    itemLabel: (T) -> String // Función para definir cómo mostrar el elemento
) {
    var expanded by remember { mutableStateOf(false) } // Estado del menú desplegable

    Box(modifier = Modifier.fillMaxWidth()) {
        // Etiqueta y selección actual
        Column(
            modifier = Modifier
                .clickable(enabled = enabled) { expanded = true }
                .alpha(if (enabled) 1f else ContentAlpha.disabled)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = selectedItem?.let(itemLabel) ?: "Seleccionar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.body1
            )
        }

        // DropdownMenu con los elementos
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                ) {
                    Text(text = itemLabel(item))
                }
            }
        }
    }
}
