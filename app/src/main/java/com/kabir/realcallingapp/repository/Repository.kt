package com.kabir.realcallingapp.repository

import android.content.Context
import android.content.Intent
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import androidx.core.net.toUri
import com.kabir.realcallingapp.model.CallLogModel
import com.kabir.realcallingapp.model.Contact

class Repository(private val context: Context) {



    fun fetchAllContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val contentResolver = context.contentResolver

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getString(idIndex) ?: ""
                val name = it.getString(nameIndex) ?: "Unknown"
                val number = it.getString(numberIndex) ?: ""

                if (number.isNotEmpty()) {
                    val cleanNumber = number.replace("\\s".toRegex(), "")
                    contactList.add(Contact(id, name, cleanNumber))
                }
            }
        }
        return contactList.distinctBy { it.phone }
    }


    fun fetchCallLogs(): List<CallLogModel> {
        val callLogs = mutableListOf<CallLogModel>()
        val contentResolver = context.contentResolver

        val projection = arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE
        )

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            val idIdx = it.getColumnIndex(CallLog.Calls._ID)
            val nameIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIdx = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext()) {
                val id = it.getString(idIdx) ?: ""
                val name = it.getString(nameIdx) ?: "Unknown"
                val number = it.getString(numberIdx) ?: ""
                val type = it.getInt(typeIdx)
                val date = it.getLong(dateIdx)
                val duration = it.getString(durationIdx) ?: "0"

                callLogs.add(CallLogModel(id, name, number, type, date, duration))
            }
        }
        return callLogs
    }

    fun placeCall(phoneNumber: String) {
        if (phoneNumber.isBlank()) return

        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = "tel:$phoneNumber".toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: SecurityException) {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:$phoneNumber".toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(dialIntent)
            Log.e("CALL_ERROR", "Permission missing, opening dialer: ${e.message}")
        }
    }
}