package com.example.stoicwecker.data

import java.time.LocalTime
import java.util.UUID

data class Alarm(
    val id: String = UUID.randomUUID().toString(),
    val time: LocalTime,
    val isEnabled: Boolean = true,
    val label: String = "",
    val daysOfWeek: Set<Int> = setOf() // Leere Menge bedeutet einmaliger Alarm
)
