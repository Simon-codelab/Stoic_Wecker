package com.example.stoicwecker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.stoicwecker.data.Alarm
import com.example.stoicwecker.viewmodel.AlarmViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AlarmViewModel,
    onAlarmTriggered: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showTimePicker by remember { mutableStateOf<String?>(null) }
    var showLabelDialog by remember { mutableStateOf<String?>(null) }
    
    // Überprüfe, ob ein Alarm ausgelöst werden sollte
    LaunchedEffect(state.alarms) {
        state.alarms.find { alarm ->
            alarm.isEnabled && alarm.time == LocalTime.now()
        }?.let { triggeredAlarm ->
            onAlarmTriggered(triggeredAlarm.id)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Stoic Wecker",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Light
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTimePicker = "" },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Wecker hinzufügen")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(state.alarms, key = { it.id }) { alarm ->
                AlarmCard(
                    alarm = alarm,
                    onTimeClick = { showTimePicker = alarm.id },
                    onLabelClick = { showLabelDialog = alarm.id },
                    onToggle = { viewModel.toggleAlarm(alarm.id) },
                    onDelete = { viewModel.removeAlarm(alarm.id) }
                )
            }
        }
    }
    
    // Time Picker Dialog
    showTimePicker?.let { alarmId ->
        TimePickerDialog(
            onDismiss = { showTimePicker = null },
            onConfirm = { hour, minute ->
                val time = LocalTime.of(hour, minute)
                if (alarmId.isEmpty()) {
                    viewModel.addAlarm(time)
                } else {
                    viewModel.updateAlarmTime(alarmId, time)
                }
                showTimePicker = null
            }
        )
    }
    
    // Label Dialog
    showLabelDialog?.let { alarmId ->
        LabelDialog(
            initialLabel = state.alarms.find { it.id == alarmId }?.label ?: "",
            onDismiss = { showLabelDialog = null },
            onConfirm = { label ->
                viewModel.updateAlarmLabel(alarmId, label)
                showLabelDialog = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmCard(
    alarm: Alarm,
    onTimeClick: () -> Unit,
    onLabelClick: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = alarm.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (alarm.isEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    },
                    modifier = Modifier.clickable(onClick = onTimeClick)
                )
                if (alarm.label.isNotEmpty()) {
                    Text(
                        text = alarm.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable(onClick = onLabelClick)
                    )
                }
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLabelClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Label bearbeiten",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { onToggle() }
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Wecker löschen",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun LabelDialog(
    initialLabel: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var label by remember { mutableStateOf(initialLabel) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Beschreibung") },
        text = {
            TextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Beschreibung eingeben") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(label) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(7) }
    var selectedMinute by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(selectedHour, selectedMinute)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        },
        title = { Text("Weckzeit einstellen") },
        text = {
            TimePicker(
                state = rememberTimePickerState(
                    initialHour = selectedHour,
                    initialMinute = selectedMinute
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
