package com.example.mygym.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mygym.ui.screens.formatDate
import java.util.Calendar

class CalendarViewModel : ViewModel() {
    private val _selectedDates = mutableStateListOf<DiaSeleccionado>()
    val selectedDates: List<DiaSeleccionado> get() = _selectedDates

    fun addSelectedDate(timestamp: Long) {
        val formattedDate = formatDate(timestamp)
        _selectedDates.add(DiaSeleccionado(timestamp, formattedDate))
    }

    fun removeSelectedDate(timestamp: Long) {
        _selectedDates.removeAll { it.timestamp == timestamp }
    }


}
