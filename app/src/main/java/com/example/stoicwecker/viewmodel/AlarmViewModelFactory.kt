package com.example.stoicwecker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stoicwecker.data.QuoteApi

class AlarmViewModelFactory(private val quoteApi: QuoteApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(quoteApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
