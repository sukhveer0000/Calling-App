package com.kabir.realcallingapp.navhost

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kabir.realcallingapp.Screen
import com.kabir.realcallingapp.screens.CallLogsScreen
import com.kabir.realcallingapp.screens.ContactsScreen
import com.kabir.realcallingapp.screens.DialerScreen

@Composable
fun NavHost() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val item = listOf(Screen.Dialer, Screen.CallLogs, Screen.Contacts)

                item.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(text = screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.CallLogs.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.CallLogs.route) { CallLogsScreen() }
            composable(Screen.Dialer.route) { DialerScreen() }
            composable(Screen.Contacts.route) { ContactsScreen() }
        }
    }
}