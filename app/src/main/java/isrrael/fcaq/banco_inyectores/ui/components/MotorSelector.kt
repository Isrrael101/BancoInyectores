// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/components/MotorSelector.kt
package isrrael.fcaq.banco_inyectores.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import isrrael.fcaq.banco_inyectores.model.Motor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background

@Composable
fun MotorSelector(
    availableMotors: List<Motor>,
    onMotorSelected: (Motor) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Seleccionar Motor Comercial") }

    Box(modifier = modifier) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedText,
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Desplegar"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            availableMotors.forEach { motor ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = motor.name,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${motor.cylinders} cilindros - Orden: ${motor.firingOrder.joinToString(",")}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    },
                    onClick = {
                        selectedText = motor.name
                        onMotorSelected(motor)
                        expanded = false
                    }
                )
            }
        }
    }
}