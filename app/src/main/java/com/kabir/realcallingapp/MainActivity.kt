package com.kabir.realcallingapp

import PermissionViewModel
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.kabir.realcallingapp.navhost.NavHost
import com.kabir.realcallingapp.ui.theme.RealCallingAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: PermissionViewModel by viewModels()

    private val permissionsToRequest = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_CALL_LOG
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RealCallingAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    val launcher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { result ->
                        viewModel.updatePermissionState(result)
                    }

                    LaunchedEffect(Unit) {
                        viewModel.checkPermissions(permissionsToRequest)
                        launcher.launch(permissionsToRequest)
                    }

                    NavHost()
                }
            }
        }
    }
}
