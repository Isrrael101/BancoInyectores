// File: app/src/main/java/isrrael/fcaq/banco_inyectores/viewmodel/MotorViewModel.kt
package isrrael.fcaq.banco_inyectores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isrrael.fcaq.banco_inyectores.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlinx.coroutines.*

class MotorViewModel : ViewModel() {

    private val availableMotors = listOf(
        Motor(
            name = "Motor Personalizado",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),
        // Motores en Línea de 4 Cilindros
        Motor(
            name = "Cummins Serie B4.5",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "John Deere 4045",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Perkins 1104",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Deutz TCD 4.1",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Kubota V3800",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),

        // Motores en Línea de 6 Cilindros
        Motor(
            name = "Caterpillar C15",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Detroit Diesel DD15",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Cummins ISX",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Volvo D13",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "MAN D2676",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Scania DC13",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),

        // Motores V8
        Motor(
            name = "Scania DC16 V8",
            cylinders = 8,
            firingOrder = listOf(1, 5, 4, 2, 6, 3, 7, 8),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "MAN D2868 V8",
            cylinders = 8,
            firingOrder = listOf(1, 5, 4, 2, 6, 3, 7, 8),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "Detroit Diesel 8V92",
            cylinders = 8,
            firingOrder = listOf(1, 5, 4, 2, 6, 3, 7, 8),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "Deutz V8 1015",
            cylinders = 8,
            firingOrder = listOf(1, 5, 4, 2, 6, 3, 7, 8),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "MTU 8V 2000",
            cylinders = 8,
            firingOrder = listOf(1, 5, 4, 2, 6, 3, 7, 8),
            configuration = MotorConfiguration.V_SHAPED
        ),

        // Motores V10
        Motor(
            name = "MAN D2840 V10",
            cylinders = 10,
            firingOrder = listOf(1, 6, 5, 10, 2, 7, 3, 8, 4, 9),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "Deutz V10 1015",
            cylinders = 10,
            firingOrder = listOf(1, 6, 5, 10, 2, 7, 3, 8, 4, 9),
            configuration = MotorConfiguration.V_SHAPED
        ),

        // Motores V12
        Motor(
            name = "MTU 12V 2000",
            cylinders = 12,
            firingOrder = listOf(1, 12, 5, 8, 3, 10, 6, 7, 2, 11, 4, 9),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "Caterpillar C27",
            cylinders = 12,
            firingOrder = listOf(1, 12, 5, 8, 3, 10, 6, 7, 2, 11, 4, 9),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "MTU 12V 4000",
            cylinders = 12,
            firingOrder = listOf(1, 12, 5, 8, 3, 10, 6, 7, 2, 11, 4, 9),
            configuration = MotorConfiguration.V_SHAPED
        ),

        // Motores Industriales Especiales
        Motor(
            name = "Wärtsilä 6L20",
            cylinders = 6,
            firingOrder = listOf(1, 4, 2, 6, 3, 5),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "ABC 8DZC",
            cylinders = 8,
            firingOrder = listOf(1, 3, 7, 4, 8, 6, 2, 5),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "EMD 8-710",
            cylinders = 8,
            firingOrder = listOf(1, 5, 3, 7, 4, 8, 2, 6),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "GE 7FDL12",
            cylinders = 12,
            firingOrder = listOf(1, 12, 7, 4, 3, 10, 6, 9, 2, 11, 5, 8),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "MaK 6M25",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Bergen B32:40",
            cylinders = 12,
            firingOrder = listOf(1, 12, 5, 8, 3, 10, 6, 7, 2, 11, 4, 9),
            configuration = MotorConfiguration.V_SHAPED
        ),
        Motor(
            name = "Rolls-Royce C25:33L",
            cylinders = 8,
            firingOrder = listOf(1, 4, 7, 2, 5, 8, 3, 6),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Yanmar 6EY22",
            cylinders = 6,
            firingOrder = listOf(1, 4, 2, 6, 3, 5),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "MAN 32/44CR",
            cylinders = 12,
            firingOrder = listOf(1, 12, 5, 8, 3, 10, 6, 7, 2, 11, 4, 9),
            configuration = MotorConfiguration.V_SHAPED
        )
    )

    // Agregar getter para los motores disponibles
    fun getAvailableMotors() = availableMotors.filter { it.name != "Motor Personalizado" }

    // Agregar función para seleccionar motor comercial
    fun selectCommercialMotor(motor: Motor) {
        _uiState.update { currentState ->
            currentState.copy(
                currentMotor = motor,
                cylinderStates = initializeCylinderStates(motor),
                customCylinders = motor.cylinders,
                customFiringOrder = motor.firingOrder.joinToString(","),
                selectedConfiguration = motor.configuration,
                isRunning = false
            )
        }
        cyclePosition = 0
    }

    private val _uiState = MutableStateFlow(
        SimulationState(
            currentMotor = availableMotors[0],
            cylinderStates = initializeCylinderStates(availableMotors[0]),
            frequency = 1f,
            isRunning = false,
            customCylinders = 4,
            customFiringOrder = "1,3,4,2",
            selectedConfiguration = MotorConfiguration.INLINE
        )
    )

    val uiState: StateFlow<SimulationState> = _uiState.asStateFlow()

    private var cyclePosition = 0
    private var simulationJob: Job? = null

    private fun initializeCylinderStates(motor: Motor): List<CylinderState> {
        val cylinderCount = motor.cylinders
        return List(cylinderCount) { index ->
            val initialPhase = (index * 4 / cylinderCount) % 4
            CylinderState(
                number = index + 1,
                phase = CylinderPhase.entries[initialPhase],
                isActive = initialPhase == 2
            )
        }
    }

    fun updateCustomMotor(cylinders: Int, firingOrder: String) {
        try {
            val firingOrderList = firingOrder.split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .filter { it in 1..cylinders }
                .distinct()
                .toMutableList()

            while (firingOrderList.size < cylinders) {
                for (i in 1..cylinders) {
                    if (!firingOrderList.contains(i)) {
                        firingOrderList.add(i)
                    }
                }
            }

            val customMotor = Motor(
                name = "Motor Personalizado",
                cylinders = cylinders,
                firingOrder = firingOrderList,
                configuration = _uiState.value.selectedConfiguration
            )

            cyclePosition = 0
            stopSimulation()

            _uiState.update { currentState ->
                currentState.copy(
                    currentMotor = customMotor,
                    cylinderStates = initializeCylinderStates(customMotor),
                    customCylinders = cylinders,
                    customFiringOrder = firingOrderList.joinToString(","),
                    isRunning = false
                )
            }
        } catch (e: Exception) {
            // Manejar el error si es necesario
        }
    }

    fun setConfiguration(configuration: MotorConfiguration) {
        _uiState.update { currentState ->
            currentState.copy(selectedConfiguration = configuration)
        }
        updateCustomMotor(_uiState.value.customCylinders, _uiState.value.customFiringOrder)
    }

    fun setFrequency(newFrequency: Float) {
        _uiState.update { it.copy(frequency = newFrequency) }
    }

    fun toggleSimulation() {
        _uiState.update { it.copy(isRunning = !it.isRunning) }
        if (_uiState.value.isRunning) {
            startSimulation()
        } else {
            stopSimulation()
        }
    }

    private fun stopSimulation() {
        simulationJob?.cancel()
        simulationJob = null
    }

    private fun startSimulation() {
        stopSimulation()
        cyclePosition = 0
        simulationJob = viewModelScope.launch {
            while (isActive && _uiState.value.isRunning) {
                try {
                    val baseDelay = (1000 / _uiState.value.frequency).roundToInt()
                    // Aseguramos un delay adecuado para visualizar el ciclo
                    delay(baseDelay.toLong().coerceAtLeast(200))
                    updateCylinderStates()
                } catch (e: Exception) {
                    stopSimulation()
                    break
                }
            }
        }
    }

    private fun updateCylinderStates() {
        try {
            _uiState.update { currentState ->
                val firingOrder = currentState.currentMotor.firingOrder
                val cylinderCount = firingOrder.size

                // Calculamos la fase base para cada posición en el orden de encendido
                val phaseOffsets = firingOrder.mapIndexed { index, cylinderNumber ->
                    val offset = (index * 4 / cylinderCount)
                    cylinderNumber to ((cyclePosition + offset) % 4)
                }.toMap()

                val updatedStates = currentState.cylinderStates.map { cylinder ->
                    // Obtenemos la fase correspondiente para este cilindro
                    val currentPhase = phaseOffsets[cylinder.number] ?: 0

                    cylinder.copy(
                        phase = CylinderPhase.entries[currentPhase],
                        isActive = currentPhase == 2  // 2 = COMBUSTION
                    )
                }

                // Avanzamos el ciclo
                cyclePosition = (cyclePosition + 1) % 4

                currentState.copy(cylinderStates = updatedStates)
            }
        } catch (e: Exception) {
            stopSimulation()
        }
    }

    fun calculateRPM(): Float {
        return _uiState.value.frequency * 60f / _uiState.value.currentMotor.cylinders
    }

    override fun onCleared() {
        super.onCleared()
        stopSimulation()
    }
}