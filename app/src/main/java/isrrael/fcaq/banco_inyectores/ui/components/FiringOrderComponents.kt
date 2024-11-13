// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/components/FiringOrderInput.kt
package isrrael.fcaq.banco_inyectores.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically

@Composable
fun FiringOrderInput(
    currentOrder: String,
    maxCylinders: Int,
    onOrderChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var orderText by remember(currentOrder) { mutableStateOf(currentOrder) }
    var errorMessage by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(4.dp)
    ) {
        Text(
            text = "Orden de Encendido ($maxCylinders cilindros):",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = orderText,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() || it == ',' || it == ' ' }
                    orderText = filteredValue
                    showError = false
                },
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = { Text("Ejemplo: 1,3,4,2") },
                singleLine = true,
                isError = showError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )

            Button(
                onClick = {
                    val numbers = orderText
                        .split(",")
                        .mapNotNull { it.trim().toIntOrNull() }
                        .filter { it in 1..maxCylinders }
                        .distinct()

                    when {
                        numbers.isEmpty() -> {
                            errorMessage = "Ingrese números válidos"
                            showError = true
                        }
                        numbers.size != maxCylinders -> {
                            errorMessage = "Debe ingresar $maxCylinders números diferentes"
                            showError = true
                        }
                        numbers.any { it > maxCylinders } -> {
                            errorMessage = "Los números deben ser del 1 al $maxCylinders"
                            showError = true
                        }
                        else -> {
                            showError = false
                            errorMessage = ""
                            onOrderChange(numbers.joinToString(","))
                        }
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Aplicar")
            }
        }

        // Mensajes de ayuda o error
        AnimatedVisibility(
            visible = showError,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Texto de ayuda
        Text(
            text = "• Ingrese $maxCylinders números diferentes separados por comas\n" +
                    "• Use números del 1 al $maxCylinders\n" +
                    "• El orden determina la secuencia de encendido",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Mostrar orden actual
        if (!showError && orderText.isNotEmpty()) {
            Text(
                text = "Secuencia actual: $orderText",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}