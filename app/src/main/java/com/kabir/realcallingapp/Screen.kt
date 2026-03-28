package com.kabir.realcallingapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dialer : Screen(route = "dialer", title = "Dialer", icon = Icons.Default.Dialpad)
    object Contacts : Screen(route = "contacts", title = "Contacts", icon = Icons.Default.Person)
    object CallLogs : Screen(route = "call_logs", title = "Logs", icon = Icons.Default.History)
}