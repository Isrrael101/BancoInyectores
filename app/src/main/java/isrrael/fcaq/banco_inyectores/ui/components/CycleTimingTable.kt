// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/components/CycleTimingTable.kt
package isrrael.fcaq.banco_inyectores.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                .fillMaxSize()
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
                        .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(5) { index ->
                        TableCell(
                            text = when(index) {
                                0 -> "Cil."
                                1 -> "Adm."
                                2 -> "Com."
                                3 -> "Exp."
                                else -> "Esc."
                            },
                            weight = 0.2f,
                            isHeader = true
                        )
                    }
                }

                // Filas de cilindros
                for (i in 1..numberOfCylinders) {
                    val cylinderState = cylinderStates.find { it.number == i }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Número de cilindro
                        TableCell(text = "$i", weight = 0.2f)

                        // Estados del cilindro
                        CylinderPhase.entries.forEach { phase ->
                            TableCell(
                                text = "",
                                weight = 0.2f,
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
            .height(18.dp)
            .background(backgroundColor.copy(alpha = 0.3f))
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}