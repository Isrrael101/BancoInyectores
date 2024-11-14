// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/screens/MotorSimulationScreen.kt
package isrrael.fcaq.banco_inyectores.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import isrrael.fcaq.banco_inyectores.model.MotorConfiguration
import isrrael.fcaq.banco_inyectores.viewmodel.MotorViewModel
import isrrael.fcaq.banco_inyectores.ui.components.FiringOrderInput
import isrrael.fcaq.banco_inyectores.ui.components.InjectorVisualization
import isrrael.fcaq.banco_inyectores.ui.components.CycleTimingTable
import isrrael.fcaq.banco_inyectores.ui.components.ColorLegend
import isrrael.fcaq.banco_inyectores.ui.components.MotorSelector

@Composable
fun MotorSimulationScreen(viewModel: MotorViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Panel de Control
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                // Selector de motor comercial
                MotorSelector(
                    availableMotors = viewModel.getAvailableMotors(),
                    onMotorSelected = viewModel::selectCommercialMotor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "Configuración del Motor",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Columna izquierda
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Cilindros: ${uiState.customCylinders}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Slider(
                            value = uiState.customCylinders.toFloat(),
                            onValueChange = {
                                viewModel.updateCustomMotor(
                                    it.toInt(),
                                    uiState.customFiringOrder
                                )
                            },
                            valueRange = 1f..12f,
                            steps = 11,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Columna derecha
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Frecuencia: ${uiState.frequency.toInt()} Hz",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Slider(
                            value = uiState.frequency,
                            onValueChange = { newValue ->
                                viewModel.setFrequency(newValue.toInt().toFloat())
                            },
                            valueRange = 1f..100f,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                // Configuración y RPM
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        MotorConfiguration.entries.forEach { config ->
                            RadioButton(
                                selected = uiState.selectedConfiguration == config,
                                onClick = { viewModel.setConfiguration(config) },
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = when(config) {
                                    MotorConfiguration.INLINE -> "Línea"
                                    MotorConfiguration.V_SHAPED -> "V"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                    Text(
                        text = "RPM: ${viewModel.calculateRPM().toInt()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Orden de encendido y matriz de selección
                FiringOrderInput(
                    currentOrder = uiState.customFiringOrder,
                    maxCylinders = uiState.customCylinders,
                    onOrderChange = { newOrder ->
                        viewModel.updateCustomMotor(
                            uiState.customCylinders,
                            newOrder
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                // Botón de inicio/parada
                Button(
                    onClick = { viewModel.toggleSimulation() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(40.dp)
                ) {
                    Text(
                        if (uiState.isRunning) "Detener" else "Iniciar",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Visualización de cilindros y leyenda
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            InjectorVisualization(
                cylinderStates = uiState.cylinderStates,
                configuration = uiState.selectedConfiguration,
                modifier = Modifier.fillMaxSize()
            )

            // Leyenda de colores en la esquina superior derecha
            ColorLegend(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 8.dp)
                    .width(80.dp)
            )
        }

        // En la parte inferior de la pantalla
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)  // Altura fija para la tabla
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            CycleTimingTable(
                numberOfCylinders = uiState.customCylinders,
                firingOrder = uiState.currentMotor.firingOrder,
                cylinderStates = uiState.cylinderStates,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
