package com.example.stoicwecker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stoicwecker.viewmodel.AlarmViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(viewModel: AlarmViewModel) {
    val state by viewModel.state.collectAsState()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Aktuelle Zeit
            Text(
                text = state.alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.displayLarge
            )
            
            // Wecker-Status
            Switch(
                checked = state.isAlarmSet,
                onCheckedChange = { viewModel.toggleAlarm() }
            )
            
            // Zitat
            state.currentQuote?.let { quote ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = quote.text,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "- ${quote.author}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            // Zeit-Picker Button
            Button(
                onClick = {
                    // Hier würde der TimePicker Dialog geöffnet werden
                }
            ) {
                Text("Weckzeit ändern")
            }
        }
    }
}
