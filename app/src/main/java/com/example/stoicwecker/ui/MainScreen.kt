package com.example.stoicwecker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.stoicwecker.viewmodel.AlarmViewModel
import java.time.format.DateTimeFormatter

@Composable
fun AlarmScreen(viewModel: AlarmViewModel, alarmId: String) {
    val state by viewModel.state.collectAsState()
    val alarm = state.alarms.find { it.id == alarmId }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Zitat oben
            state.currentQuote?.let { quote ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = quote.text,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "- ${quote.author}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.End),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            // Aktuelle Zeit (mittig)
            alarm?.let { currentAlarm ->
                Text(
                    text = currentAlarm.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Light
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 48.dp)
                )
                
                if (currentAlarm.label.isNotEmpty()) {
                    Text(
                        text = currentAlarm.label,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
                
                // Buttons nebeneinander
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Alarm Ein/Aus Button
                    OutlinedButton(
                        onClick = { viewModel.toggleAlarm(currentAlarm.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Ausschalten",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    // Snooze Button
                    FilledTonalButton(
                        onClick = { viewModel.snoozeAlarm(currentAlarm.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Snooze",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
