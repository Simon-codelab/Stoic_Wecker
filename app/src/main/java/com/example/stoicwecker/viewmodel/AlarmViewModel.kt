package com.example.stoicwecker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stoicwecker.data.Alarm
import com.example.stoicwecker.data.Quote
import com.example.stoicwecker.data.QuoteApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

data class AlarmState(
    val alarms: List<Alarm> = emptyList(),
    val currentQuote: Quote? = null
)

class AlarmViewModel(private val quoteApi: QuoteApi) : ViewModel() {
    private val _state = MutableStateFlow(AlarmState())
    val state: StateFlow<AlarmState> = _state

    init {
        fetchNewQuote()
        // Beispiel-Wecker zum Testen
        addAlarm(LocalTime.of(7, 0))
        addAlarm(LocalTime.of(8, 30))
    }

    fun addAlarm(time: LocalTime, label: String = "") {
        val newAlarm = Alarm(
            id = UUID.randomUUID().toString(),
            time = time,
            label = label,
            isEnabled = true
        )
        _state.value = _state.value.copy(
            alarms = _state.value.alarms + newAlarm
        )
        // Hier würde die Logik zum Setzen des tatsächlichen Alarms kommen
    }

    fun removeAlarm(alarmId: String) {
        _state.value = _state.value.copy(
            alarms = _state.value.alarms.filter { it.id != alarmId }
        )
        // Hier würde die Logik zum Entfernen des tatsächlichen Alarms kommen
    }

    fun toggleAlarm(alarmId: String) {
        _state.value = _state.value.copy(
            alarms = _state.value.alarms.map { alarm ->
                if (alarm.id == alarmId) {
                    alarm.copy(isEnabled = !alarm.isEnabled)
                } else {
                    alarm
                }
            }
        )
        // Hier würde die Logik zum Aktivieren/Deaktivieren des tatsächlichen Alarms kommen
    }

    fun updateAlarmTime(alarmId: String, newTime: LocalTime) {
        _state.value = _state.value.copy(
            alarms = _state.value.alarms.map { alarm ->
                if (alarm.id == alarmId) {
                    alarm.copy(time = newTime)
                } else {
                    alarm
                }
            }
        )
        // Hier würde die Logik zum Aktualisieren des tatsächlichen Alarms kommen
    }

    fun updateAlarmLabel(alarmId: String, newLabel: String) {
        _state.value = _state.value.copy(
            alarms = _state.value.alarms.map { alarm ->
                if (alarm.id == alarmId) {
                    alarm.copy(label = newLabel)
                } else {
                    alarm
                }
            }
        )
    }

    fun fetchNewQuote() {
        viewModelScope.launch {
            try {
                val quote = quoteApi.getRandomQuote()
                _state.value = _state.value.copy(currentQuote = quote)
            } catch (e: Exception) {
                // Fehlerbehandlung
            }
        }
    }

    fun snoozeAlarm(alarmId: String) {
        val alarm = _state.value.alarms.find { it.id == alarmId } ?: return
        val newTime = alarm.time.plusMinutes(5)
        updateAlarmTime(alarmId, newTime)
    }
}

data class Alarm(
    val id: String,
    val time: LocalTime,
    val label: String,
    val isEnabled: Boolean
)
