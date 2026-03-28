package com.kabir.realcallingapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kabir.realcallingapp.model.CallLogModel
import com.kabir.realcallingapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CallLogViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application.applicationContext)

    var callLogs by mutableStateOf<List<CallLogModel>>(emptyList())
    var isLoading by mutableStateOf(false)

    fun fetchLogs(force: Boolean = false) {
        if (isLoading && !force) return

        isLoading = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = repository.fetchCallLogs()

                withContext(Dispatchers.Main) {
                    callLogs = data
                    isLoading = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }

    fun makeCall(number: String) {
        repository.placeCall(number)
    }
}