package isrrael.fcaq.banco_inyectores.model

data class Motor(
    val name: String,
    val cylinders: Int,
    val firingOrder: List<Int>,
    val configuration: MotorConfiguration
)

enum class MotorConfiguration {
    INLINE, V_SHAPED
}

data class CylinderState(
    val number: Int,
    val phase: CylinderPhase,
    val isActive: Boolean = false
)

enum class CylinderPhase {
    INTAKE, COMPRESSION, COMBUSTION, EXHAUST;

    fun getColor(): Long = when(this) {
        INTAKE -> 0xFF90EE90      // Verde claro para admisión
        COMPRESSION -> 0xFFFFB6C1  // Rosa claro para compresión
        COMBUSTION -> 0xFFFF6B6B   // Rojo para combustión
        EXHAUST -> 0xFF87CEEB      // Azul claro para escape
    }

    fun getDescription(): String = when(this) {
        INTAKE -> "Admisión"
        COMPRESSION -> "Compresión"
        COMBUSTION -> "Explosión"
        EXHAUST -> "Escape"
    }
}

data class SimulationState(
    val currentMotor: Motor,
    val cylinderStates: List<CylinderState>,
    val frequency: Float = 1f,
    val isRunning: Boolean = false
)