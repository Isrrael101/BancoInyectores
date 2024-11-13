// File: app/src/main/java/isrrael/fcaq/banco_inyectores/MainActivity.kt
package isrrael.fcaq.banco_inyectores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import isrrael.fcaq.banco_inyectores.ui.theme.BancoInyectoresTheme
import isrrael.fcaq.banco_inyectores.viewmodel.MotorViewModel
import isrrael.fcaq.banco_inyectores.ui.screens.MotorSimulationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BancoInyectoresTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MotorViewModel = viewModel()
                    MotorSimulationScreen(viewModel = viewModel)
                }
            }
        }
    }
}