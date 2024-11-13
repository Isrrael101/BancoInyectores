// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/screens/MotorSimulationScreen.kt
package isrrael.fcaq.banco_inyectores.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import isrrael.fcaq.banco_inyectores.model.CylinderPhase
import isrrael.fcaq.banco_inyectores.model.CylinderState
import isrrael.fcaq.banco_inyectores.viewmodel.MotorViewModel

@Composable
fun MotorSimulationScreen(viewModel: MotorViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Panel de Control
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Control del Motor",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Control de frecuencia
                Text(
                    text = "Frecuencia: ${uiState.frequency.toInt()} Hz",
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = uiState.frequency,
                    onValueChange = { viewModel.setFrequency(it) },
                    valueRange = 0.1f..100f,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Display RPM
                Text(
                    text = "RPM: ${viewModel.calculateRPM().toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Botón de inicio/parada
                Button(
                    onClick = { viewModel.toggleSimulation() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(if (uiState.isRunning) "Detener" else "Iniciar")
                }
            }
        }

        // Visualización de cilindros
        InjectorVisualization(
            cylinderStates = uiState.cylinderStates,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun InjectorVisualization(
    cylinderStates: List<CylinderState>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cylinderWidth = size.width / (cylinderStates.size + 1)
        val cylinderHeight = cylinderWidth * 2
        val startY = (size.height - cylinderHeight) / 2

        cylinderStates.forEachIndexed { index, state ->
            val startX = cylinderWidth * (index + 0.5f)
            drawInjector(
                centerX = startX,
                centerY = startY + (cylinderHeight / 2),
                width = cylinderWidth * 0.8f,
                height = cylinderHeight,
                state = state
            )
        }
    }
}

private fun DrawScope.drawInjector(
    centerX: Float,
    centerY: Float,
    width: Float,
    height: Float,
    state: CylinderState
) {
    // Cuerpo del inyector
    drawRect(
        color = Color(state.phase.getColor()),
        topLeft = Offset(centerX - width/2, centerY - height/2),
        size = Size(width, height)
    )

    // Número del cilindro
    drawCircle(
        color = Color.White,
        radius = width * 0.2f,
        center = Offset(centerX, centerY - height * 0.3f)
    )

    // Efecto de spray cuando está en fase de combustión
    if (state.phase == CylinderPhase.COMBUSTION) {
        drawRect(
            color = Color.Yellow,
            topLeft = Offset(centerX - width/6, centerY + height/2),
            size = Size(width/3, height/4)
        )
    }
}