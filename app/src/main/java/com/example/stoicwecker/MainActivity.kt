package com.example.stoicwecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stoicwecker.data.QuoteApiImpl
import com.example.stoicwecker.ui.HomeScreen
import com.example.stoicwecker.ui.AlarmScreen
import com.example.stoicwecker.ui.theme.StoicWeckerTheme
import com.example.stoicwecker.viewmodel.AlarmViewModel
import com.example.stoicwecker.viewmodel.AlarmViewModelFactory

sealed class Screen {
    object Home : Screen()
    data class Alarm(val alarmId: String) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val quoteApi = QuoteApiImpl()
        val factory = AlarmViewModelFactory(quoteApi)
        
        setContent {
            StoicWeckerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AlarmViewModel = viewModel(factory = factory)
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
                    
                    when (currentScreen) {
                        is Screen.Home -> HomeScreen(
                            viewModel = viewModel,
                            onAlarmTriggered = { alarmId ->
                                currentScreen = Screen.Alarm(alarmId)
                            }
                        )
                        is Screen.Alarm -> AlarmScreen(
                            viewModel = viewModel,
                            alarmId = (currentScreen as Screen.Alarm).alarmId
                        )
                    }
                }
            }
        }
    }
}