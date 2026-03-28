package com.kabir.realcallingapp.viewmodel


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.kabir.realcallingapp.repository.Repository

class DialerViewModel(application: Application) : AndroidViewModel(application) {


    private val repository = Repository(application.applicationContext)

     var phoneNumber by mutableStateOf("")
        private set

    fun onDigitClick(digit: String) {
        if (phoneNumber.length < 15) {
            phoneNumber += digit
        }
    }

    fun onDeleteClick() {
        if (phoneNumber.isNotEmpty()) {
            phoneNumber = phoneNumber.dropLast(1)
        }
    }

    fun onCallClick() {
        if (phoneNumber.isNotEmpty()) {
            repository.placeCall(phoneNumber)
        }
    }
}