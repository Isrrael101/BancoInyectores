// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/components/InjectorVisualization.kt
package isrrael.fcaq.banco_inyectores.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import isrrael.fcaq.banco_inyectores.model.CylinderPhase
import isrrael.fcaq.banco_inyectores.model.CylinderState
import isrrael.fcaq.banco_inyectores.model.MotorConfiguration

@Composable
fun InjectorVisualization(
    cylinderStates: List<CylinderState>,
    configuration: MotorConfiguration,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val baseWidth = 120f
        val maxAvailableWidth = size.width * 0.9f
        val cylinderWidth = when(configuration) {
            MotorConfiguration.INLINE -> {
                val spacingFactor = 1.4f
                val totalSpacing = cylinderStates.size * spacingFactor
                minOf(baseWidth, maxAvailableWidth / totalSpacing)
            }
            MotorConfiguration.V_SHAPED -> baseWidth
        }

        val cylinderHeight = cylinderWidth * 2
        val cylinderSpacing = cylinderWidth * 0.4f

        when(configuration) {
            MotorConfiguration.INLINE -> {
                val startY = (size.height - cylinderHeight) / 2
                val totalWidth = (cylinderStates.size * cylinderWidth) +
                        ((cylinderStates.size - 1) * cylinderSpacing)
                val startX = (size.width - totalWidth) / 2

                cylinderStates.forEachIndexed { index, state ->
                    val cylinderX = startX +
                            (index * (cylinderWidth + cylinderSpacing)) +
                            (cylinderWidth / 2)
                    drawInjector(
                        centerX = cylinderX,
                        centerY = startY + (cylinderHeight / 2),
                        width = cylinderWidth * 0.8f,
                        height = cylinderHeight,
                        state = state
                    )
                }
            }
            MotorConfiguration.V_SHAPED -> {
                val halfCylinders = cylinderStates.size / 2
                val totalWidth = (halfCylinders * cylinderWidth) +
                        ((halfCylinders - 1) * cylinderSpacing)
                val startX = (size.width - totalWidth) / 2

                val centerY = size.height / 2
                val vOffset = cylinderHeight * 0.8f

                cylinderStates.forEachIndexed { index, state ->
                    val isTopRow = index < cylinderStates.size / 2
                    val rowIndex = if (isTopRow) index else index - cylinderStates.size / 2
                    val cylinderX = startX +
                            (rowIndex * (cylinderWidth + cylinderSpacing)) +
                            (cylinderWidth / 2)
                    val cylinderY = if (isTopRow) centerY - vOffset else centerY + vOffset

                    drawInjector(
                        centerX = cylinderX,
                        centerY = cylinderY,
                        width = cylinderWidth * 0.8f,
                        height = cylinderHeight,
                        state = state
                    )
                }
            }
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

    // Círculo blanco para el número
    drawCircle(
        color = Color.White,
        radius = width * 0.25f,
        center = Offset(centerX, centerY - height * 0.3f)
    )

    // Círculo negro más pequeño para el número
    drawCircle(
        color = Color.Black,
        radius = width * 0.15f,
        center = Offset(centerX, centerY - height * 0.3f)
    )

    // Efecto de spray
    if (state.phase == CylinderPhase.COMBUSTION) {
        drawRect(
            color = Color.Yellow,
            topLeft = Offset(centerX - width/4, centerY + height/2),
            size = Size(width/2, height/3)
        )
    }
}