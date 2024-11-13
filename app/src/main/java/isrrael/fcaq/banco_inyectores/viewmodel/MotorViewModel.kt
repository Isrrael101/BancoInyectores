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

class MotorViewModel : ViewModel() {

    private val availableMotors = listOf(
        Motor(
            name = "Motor Personalizado",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Cummins Serie B (4 cilindros)",
            cylinders = 4,
            firingOrder = listOf(1, 3, 4, 2),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Caterpillar C15 (6 cilindros)",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Detroit Diesel Serie 60 (6 cilindros)",
            cylinders = 6,
            firingOrder = listOf(1, 5, 3, 6, 2, 4),
            configuration = MotorConfiguration.INLINE
        ),
        Motor(
            name = "Scania V8 (8 cilindros)",
            cylinders = 8,
            firingOrder = listOf(1, 5, 4, 2, 6, 3, 7, 8),
            configuration = MotorConfiguration.V_SHAPED
        )
    )

    private val _uiState = MutableStateFlow(
        SimulationState(
            currentMotor = availableMotors[0],
            cylinderStates = initializeCylinderStates(availableMotors[0])
        )
    )

    val uiState: StateFlow<SimulationState> = _uiState.asStateFlow()

    private fun initializeCylinderStates(motor: Motor): List<CylinderState> {
        return List(motor.cylinders) { index ->
            CylinderState(
                number = index + 1,
                phase = CylinderPhase.values()[index % 4]
            )
        }
    }

    fun updateCylinderCount(count: Int) {
        if (count in 1..12) {
            val newFiringOrder = generateDefaultFiringOrder(count)
            val newMotor = Motor(
                name = "Motor Personalizado",
                cylinders = count,
                firingOrder = newFiringOrder,
                configuration = if (count <= 6) MotorConfiguration.INLINE else MotorConfiguration.V_SHAPED
            )
            setMotor(newMotor)
        }
    }

    fun updateFiringOrder(newOrder: String) {
        try {
            val orderNumbers = newOrder.split(",").map { it.trim().toInt() }
            if (isValidFiringOrder(orderNumbers)) {
                val currentMotor = _uiState.value.currentMotor
                val newMotor = currentMotor.copy(firingOrder = orderNumbers)
                setMotor(newMotor)
            }
        } catch (e: Exception) {
            // Manejar error de formato
        }
    }

    private fun isValidFiringOrder(order: List<Int>): Boolean {
        val currentCylinders = _uiState.value.currentMotor.cylinders
        return order.size == currentCylinders &&
                order.toSet() == (1..currentCylinders).toSet()
    }

    private fun generateDefaultFiringOrder(cylinderCount: Int): List<Int> {
        return when (cylinderCount) {
            4 -> listOf(1, 3, 4, 2)
            6 -> listOf(1, 5, 3, 6, 2, 4)
            8 -> listOf(1, 5, 4, 2, 6, 3, 7, 8)
            12 -> listOf(1, 12, 5, 8, 3, 10, 6, 7, 2, 11, 4, 9)
            else -> (1..cylinderCount).toList() // Orden secuencial para otros casos
        }
    }

    fun setMotor(motor: Motor) {
        _uiState.update { currentState ->
            currentState.copy(
                currentMotor = motor,
                cylinderStates = initializeCylinderStates(motor),
                isRunning = false
            )
        }
    }

    fun setFrequency(newFrequency: Float) {
        _uiState.update { it.copy(frequency = newFrequency) }
    }

    fun toggleSimulation() {
        _uiState.update { it.copy(isRunning = !it.isRunning) }
        if (_uiState.value.isRunning) {
            startSimulation()
        }
    }

    private fun startSimulation() {
        viewModelScope.launch {
            while (_uiState.value.isRunning) {
                val delayTime = (1000 / (_uiState.value.frequency * 2)).roundToInt()
                delay(delayTime.toLong())
                updateCylinderStates()
            }
        }
    }

    private fun updateCylinderStates() {
        _uiState.update { currentState ->
            val updatedStates = currentState.cylinderStates.map { cylinderState ->
                val currentPhaseIndex = CylinderPhase.values().indexOf(cylinderState.phase)
                val nextPhaseIndex = (currentPhaseIndex + 1) % 4
                cylinderState.copy(
                    phase = CylinderPhase.values()[nextPhaseIndex],
                    isActive = currentState.currentMotor.firingOrder.indexOf(cylinderState.number) == currentPhaseIndex
                )
            }
            currentState.copy(cylinderStates = updatedStates)
        }
    }

    fun calculateRPM(): Float {
        return _uiState.value.frequency * 60f / _uiState.value.currentMotor.cylinders
    }
}