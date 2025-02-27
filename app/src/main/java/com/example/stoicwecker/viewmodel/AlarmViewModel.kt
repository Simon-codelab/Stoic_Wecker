package com.example.stoicwecker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stoicwecker.data.Quote
import com.example.stoicwecker.data.QuoteApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime

data class AlarmState(
    val isAlarmSet: Boolean = false,
    val alarmTime: LocalTime = LocalTime.of(7, 0),
    val currentQuote: Quote? = null
)

class AlarmViewModel(private val quoteApi: QuoteApi) : ViewModel() {
    private val _state = MutableStateFlow(AlarmState())
    val state: StateFlow<AlarmState> = _state

    fun setAlarmTime(hour: Int, minute: Int) {
        _state.value = _state.value.copy(
            isAlarmSet = true,
            alarmTime = LocalTime.of(hour, minute)
        )
        // Hier würde die Logik zum Setzen des tatsächlichen Alarms kommen
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

    fun toggleAlarm() {
        _state.value = _state.value.copy(isAlarmSet = !_state.value.isAlarmSet)
    }
}
