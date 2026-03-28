package com.kabir.realcallingapp.screens

import PermissionViewModel
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kabir.realcallingapp.model.CallLogModel
import com.kabir.realcallingapp.viewmodel.CallLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLogsScreen(
    logViewModel: CallLogViewModel = viewModel(),
    @SuppressLint("ContextCastToActivity")
    pViewModel: PermissionViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
) {
    val state = pViewModel.permissionState
    val logPerm = android.Manifest.permission.READ_CALL_LOG
    val logs = logViewModel.callLogs
    val isLoading = logViewModel.isLoading
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current


    LaunchedEffect(state.grantedPermissions) {
        if (state.grantedPermissions.contains(logPerm)) {
            logViewModel.fetchLogs()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                if (state.grantedPermissions.contains(logPerm)) {
                    logViewModel.fetchLogs(force = true)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        pViewModel.updatePermissionState(mapOf(logPerm to isGranted))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Call Logs",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {

            if (!state.grantedPermissions.contains(logPerm)) {

                PermissionPlaceholder(
                    title = "Call Logs Access Required",
                    onAllowClick = { launcher.launch(logPerm) }
                )

            } else {

                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    logs.isNotEmpty() -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(logs) { log ->
                                CallLogItem(log = log, viewModel = logViewModel)
                            }
                        }
                    }

                    else -> {
                        Text(
                            "No Call Logs Found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PermissionPlaceholder(title: String, onAllowClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAllowClick) {
            Text("Allow Permission")
        }
    }
}

@Composable
fun CallLogItem(log: CallLogModel, viewModel: CallLogViewModel) {
    val (icon, color) = when (log.type) {
        1 -> Icons.Default.CallReceived to Color(0xFF4CAF50)
        2 -> Icons.Default.CallMade to Color(0xFF2196F3)
        3 -> Icons.Default.CallMissed to Color(0xFFF44336)
        else -> Icons.Default.Call to Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.makeCall(log.number) }
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = Color(0xFFE8EAED) // Light Grey background
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = Color(0xFF5F6368)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (log.name == "Unknown" || log.name.isEmpty()) log.number else log.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = if (log.type == 3) Color.Red else Color.Black
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mobile • ${getFormattedTime(log.date)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            IconButton(
                onClick = { viewModel.makeCall(log.number) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Again",
                    tint = Color(0xFF1A73E8),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(start = 80.dp, end = 16.dp),
            thickness = 0.5.dp,
            color = Color.LightGray.copy(alpha = 0.4f)
        )
    }
}

fun getFormattedTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000} min ago"
        isSameDay(now, timestamp) -> {
            java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
                .format(java.util.Date(timestamp))
        }

        else -> {
            java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
                .format(java.util.Date(timestamp))
        }
    }
}

fun isSameDay(t1: Long, t2: Long): Boolean {
    val fmt = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
    return fmt.format(java.util.Date(t1)) == fmt.format(java.util.Date(t2))
}

