// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/components/CycleTimingTable.kt
package isrrael.fcaq.banco_inyectores.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isrrael.fcaq.banco_inyectores.model.CylinderPhase
import isrrael.fcaq.banco_inyectores.model.CylinderState

@Composable
fun CycleTimingTable(
    numberOfCylinders: Int,
    firingOrder: List<Int>,
    cylinderStates: List<CylinderState>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth()
        ) {
            // Contenedor con scroll para la tabla
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Cabecera de la tabla
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    TableCell("Cil.", 0.15f, true)
                    TableCell("Admisión", 0.2125f, true)
                    TableCell("Compresión", 0.2125f, true)
                    TableCell("Explosión", 0.2125f, true)
                    TableCell("Escape", 0.2125f, true)
                }

                // Filas de cilindros
                for (i in 1..numberOfCylinders) {
                    val cylinderState = cylinderStates.find { it.number == i }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        TableCell("Cil. $i", 0.15f)

                        // Colorear la celda actual según la fase del cilindro
                        CylinderPhase.entries.forEach { phase ->
                            TableCell(
                                text = "",
                                weight = 0.2125f,
                                backgroundColor = if (cylinderState?.phase == phase) {
                                    Color(phase.getColor())
                                } else {
                                    Color.Transparent
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
    backgroundColor: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(weight)
            .height(16.dp) // Altura fija para las celdas
            .background(backgroundColor.copy(alpha = 0.3f))
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            .padding(1.dp)
    ) {
        Text(
            text = text,
            fontSize = 6.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}