package com.example.contador

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContadorViewModel : ViewModel() {

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment(step: Int = 1) {
        _count.value += step
    }

    fun decrement(step: Int = 1) {
        _count.value -= step
    }

    fun reset() {
        _count.value = 0
    }
}
