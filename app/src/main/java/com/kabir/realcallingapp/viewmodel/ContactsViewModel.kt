package com.kabir.realcallingapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kabir.realcallingapp.model.Contact
import com.kabir.realcallingapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application.applicationContext)

    var contactsList by mutableStateOf<List<Contact>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun fetchContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    isLoading = true
                    errorMessage = null
                }

                val data = repository.fetchAllContacts()

                withContext(Dispatchers.Main) {
                    contactsList = data
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Error: ${e.message}"
                }
            } finally {
                // Loading ko Main thread par stop karo
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