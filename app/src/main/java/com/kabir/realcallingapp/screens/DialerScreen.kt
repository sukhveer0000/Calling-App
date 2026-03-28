package com.kabir.realcallingapp.screens

import PermissionViewModel
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kabir.realcallingapp.viewmodel.DialerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialerScreen(
    viewModel: DialerViewModel = viewModel(),
    @SuppressLint("ContextCastToActivity")
    pViewModel: PermissionViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
) {
    val state = pViewModel.permissionState
    val callPerm = android.Manifest.permission.CALL_PHONE
    val phoneNumber = viewModel.phoneNumber

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        pViewModel.updatePermissionState(mapOf(callPerm to isGranted))
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (state.grantedPermissions.contains(callPerm)) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = phoneNumber,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    keys.chunked(3).forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            row.forEach { digit ->
                                DialerButton(digit) {
                                    viewModel.onDigitClick(digit)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(60.dp))

                        FloatingActionButton(
                            onClick = { viewModel.onCallClick() },
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White,
                            shape = CircleShape,
                            modifier = Modifier.size(70.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(30.dp))
                        }

                        IconButton(
                            onClick = { viewModel.onDeleteClick() },
                            modifier = Modifier.size(60.dp)
                        ) {
                            Icon(Icons.Default.Backspace, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                }
            }
        } else {
             PermissionPlaceholder(
                title = "Phone Calling Access",
                onAllowClick = { launcher.launch(callPerm) }
            )
        }
    }
}
@Composable
fun DialerButton(digit: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .size(85.dp)
            .padding(8.dp),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = digit, style = MaterialTheme.typography.headlineMedium)
        }
    }
}