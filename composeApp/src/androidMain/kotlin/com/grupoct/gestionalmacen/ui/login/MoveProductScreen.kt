package com.grupoct.gestionalmacen.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.grupoct.gestionalmacen.data.Move
import com.grupoct.gestionalmacen.data.Product
import com.grupoct.gestionalmacen.data.Vitrine
import com.grupoct.gestionalmacen.data.Warehouse
import com.grupoct.gestionalmacen.viewmodel.WarehouseViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable

fun MoveProductScreen(  viewModel: WarehouseViewModel,
     token: String
)

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

    // Mostrar mensajes según el resultado
    val moveResult by viewModel.moveResult.collectAsState()


    // Estado para el Dialog
    val showDialogError = remember { mutableStateOf(false) }
    val showDialogErrorBusqueda = remember { mutableStateOf(false) }

    val dialogMessage = remember { mutableStateOf("") }
    val showDialogConfirm = remember { mutableStateOf(false) }

    // Observamos el estado de error del ViewModel
    val errorMessage by viewModel.error.collectAsState()
    // Si hay un error, mostramos el Dialog
    errorMessage?.let {
        dialogMessage.value = it
        showDialogErrorBusqueda.value = true
    }



// Mostrar mensajes según el resultado
    if (moveResult != null) {
        moveResult?.let { result ->
            AlertDialog(
                onDismissRequest = {
                    viewModel.resetMoveResult() // Reinicia el estado del resultado
                    if (result.isSuccess) {
                        resetFields(selectedOrigin, selectedDestination, selectedVitrine, moveQuantity, viewModel, productCode)
                    }
                },
                title = {
                    Text(
                        if (result.isSuccess)
                            "Traslado Exitoso del producto: \n" +
                                    "${productDetails?.descripcion} \n" +
                                    "Codigo: ${productDetails?.codigo} \n" +
                                    "Almacen Origen: ${selectedOrigin.value?.nombreTienda}\n" +
                                    "Almacen Destino: ${selectedDestination.value?.nombreTienda}\n" +
                                    "Vitrina: ${selectedVitrine.value?.nombreVitrina}\n" +
                                    "Cantidad: ${moveQuantity.value}"
                        else "Error en el Traslado"
                    )
                },
                text = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center // Centra todo el contenido
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                result.getOrNull() ?: result.exceptionOrNull()?.message ?: "Ha ocurrido un error desconocido",
                                textAlign = TextAlign.Center // Centra el texto si es muy largo
                            )
                            if (result.isSuccess) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Traslado exitoso",
                                    tint = Color.Green,
                                    modifier = Modifier.size(48.dp) // Tamaño del ícono
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetMoveResult() // Reinicia el estado del resultado
                            if (result.isSuccess) {
                                resetFields(selectedOrigin, selectedDestination, selectedVitrine, moveQuantity, viewModel, productCode)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Blue
                        )
                    ) {
                        Text("Aceptar" , color = Color.White)
                    }
                }
            )


        }
    }


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

    // Diseño de la pantalla con fondo de gradiente azul
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF336DE1),
                        Color(0xFFEF2F2F)
                    )
                )
            ))
            .padding(16.dp)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(), // Aseguramos que la columna ocupe todo el ancho
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido de la columna horizontalmente
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            //titlulo: Movimiento
            Text(
                text = "MOVIMIENTO DE PRODUCTO",
                style = MaterialTheme.typography.h6.copy(color = Color.White),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )



            Spacer(modifier = Modifier.height(26.dp))

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
                label = { Text("Código de Producto", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White, // Color del texto ingresado
                    cursorColor = Color.White, // Color del cursor
                    focusedBorderColor = Color.White, // Borde blanco al enfocarse
                    unfocusedBorderColor = Color.White // Borde blanco sin enfoque
                )
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
                enabled = selectedOrigin.value != null && productCode.value.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                )
            ) {
                Text("Buscar Producto" ,color = Color.Blue)
            }



            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar descripción del producto
            val stockAvailable = productDetails?.stock ?: 0
            val productDescription = productDetails?.descripcion ?: "Sin descripcion"

            Text(text = "Producto: $productDescription", style = MaterialTheme.typography.body1
            ,color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Stock Disponible: $stockAvailable", style = MaterialTheme.typography.body1
            ,color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            // Input: Cantidad
            OutlinedTextField(
                value = moveQuantity.value,

                onValueChange = {
                    newValue ->
                    // Validar que el nuevo valor sea solo números
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        moveQuantity.value = newValue
                    }
                },
                label = { Text("Cantidad a Mover" , color = Color.White ) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))



            // Botón: Mover Producto
            Button( onClick = {
                val quantity = moveQuantity.value.toIntOrNull() ?: 0

                if (quantity > stockAvailable) {
                    // Si la cantidad a mover es mayor que el stock, mostrar el dialog
                    dialogMessage.value = "La cantidad a mover no puede ser mayor que el stock disponible ($stockAvailable)."
                    showDialogError.value = true
                } else {

                    // Si la validación pasa, mostrar el diálogo de confirmación
                    showDialogConfirm.value = true
                }
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedOrigin.value != null &&
                        selectedDestination.value != null &&
                        selectedVitrine.value != null &&
                        productDetails != null &&
                        moveQuantity.value.toIntOrNull() != null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White
                )
            ) {
                Text("Hacer Traslado", color = Color.Blue)
            }

            // Mostrar Dialog si la cantidad excede el stock
            if (showDialogError.value) {
                AlertDialog(
                    onDismissRequest = { showDialogError.value = false },
                    title = { Text("Error en stock") },
                    text = { Text(dialogMessage.value) },
                    confirmButton = {
                        Button(onClick = { showDialogError.value = false }) {
                            Text("Cerrar")
                        }
                    }
                )
            }

            // Mostrar el Dialog de error si showDialogError es true
            if (showDialogErrorBusqueda.value) {
                AlertDialog(
                    onDismissRequest = { showDialogErrorBusqueda.value = false
                        viewModel.clearError()
                    },
                    title = { Text("Error de busqueda") },
                    text = { Text(dialogMessage.value) },
                    confirmButton = {
                        Button(onClick = { showDialogErrorBusqueda.value = false
                            viewModel.clearError() }) {
                            Text("Cerrar")
                        }
                    }
                )
            }

            // Dialogo de confirmación

            // Dialogo de confirmación
            if (showDialogConfirm.value) {
                AlertDialog(
                    onDismissRequest = { showDialogConfirm.value = false },
                    title = { Text("Confirmación") },
                    text = { Text("¿Está seguro de que desea trasladar el producto?") },
                    confirmButton = {
                        Button(onClick = {
                            showDialogConfirm.value = false
                            // Ejecutar la acción de mover el producto
                            val move = Move(
                                idproducto = productDetails?.idproducto ?: 0,
                                idalmacenorigen = selectedOrigin.value?.idtienda ?: 0,
                                idalmacendestino = selectedDestination.value?.idtienda ?: 0,
                                cantidad = moveQuantity.value.toIntOrNull() ?: 0,
                                vitrina = selectedVitrine.value?.nombreVitrina ?: "",
                                idusuario = userId
                            )
                            viewModel.moveProduct(token, move)
                        }) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialogConfirm.value = false }) {
                            Text("No")
                        }
                    }
                )
            }



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
    itemLabel: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) } // Estado del menú desplegable

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled) { expanded = true } // Abre el menú al hacer clic en cualquier parte
    ) {
        val dropdownWidth = maxWidth // Usa el ancho del Box como referencia

        OutlinedTextField(
            value = selectedItem?.let(itemLabel) ?: "",
            onValueChange = {},
            readOnly = true, // Evita que el usuario escriba directamente
            enabled = enabled,
            label = { Text(label, color = Color.White) },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }, // Abre o cierra el menú con el ícono
                    tint = Color.White
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White
            )
        )

        // Menú desplegable
        androidx.compose.material.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, // Cierra el menú al tocar fuera
            modifier = Modifier.width(dropdownWidth) // Ajusta el ancho del menú desplegable
        ) {
            items.forEach { item ->
                androidx.compose.material.DropdownMenuItem(
                    onClick = {
                        onItemSelected(item) // Llama al evento al seleccionar un item
                        expanded = false // Cierra el menú después de seleccionar
                    }
                ) {
                    Text(text = itemLabel(item)) // Muestra el nombre del item
                }
            }
        }
    }
}



// Función para reiniciar los campos
private fun resetFields(
    selectedOrigin: MutableState<Warehouse?>,
    selectedDestination: MutableState<Warehouse?>,
    selectedVitrine: MutableState<Vitrine?>,
    moveQuantity: MutableState<String>,
    viewModel: WarehouseViewModel,
    //el input para buscar :
    productCode: MutableState<String>

) {
    selectedOrigin.value = null
    selectedDestination.value = null
    selectedVitrine.value = null
    moveQuantity.value = ""
    viewModel.resetProductDetails()
    productCode.value = ""
}
