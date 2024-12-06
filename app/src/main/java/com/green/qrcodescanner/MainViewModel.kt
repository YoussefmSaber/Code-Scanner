package com.green.qrcodescanner

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {

    private val _scanResult = MutableStateFlow("")
    val scanResult = _scanResult.asStateFlow()

    fun onScanResult(result: String) {
        _scanResult.value = result ?: "Cancelled"
    }
}