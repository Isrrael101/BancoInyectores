// File: app/src/main/java/isrrael/fcaq/banco_inyectores/ui/components/ColorLegend.kt
package isrrael.fcaq.banco_inyectores.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isrrael.fcaq.banco_inyectores.model.CylinderPhase

@Composable
fun ColorLegend(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Fases:",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            CylinderPhase.entries.forEach { phase ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                Color(phase.getColor()),
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when(phase) {
                            CylinderPhase.INTAKE -> "Admisión"
                            CylinderPhase.COMPRESSION -> "Compresión"
                            CylinderPhase.COMBUSTION -> "Expansión"
                            CylinderPhase.EXHAUST -> "Escape"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp
                    )
                }
            }
        }
    }
}