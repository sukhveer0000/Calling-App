package com.kabir.realcallingapp.screens

import PermissionViewModel
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kabir.realcallingapp.model.Contact
import com.kabir.realcallingapp.viewmodel.ContactsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel = viewModel(),
    @SuppressLint("ContextCastToActivity")
    pViewModel: PermissionViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
) {
    val state = pViewModel.permissionState
    val contactPerm = android.Manifest.permission.READ_CONTACTS
    val contacts = viewModel.contactsList
    val isLoading = viewModel.isLoading

    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        pViewModel.updatePermissionState(mapOf(contactPerm to isGranted))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Contacts",
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
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {

            if (state.grantedPermissions.contains(contactPerm)) {

                LaunchedEffect(Unit) {
                    viewModel.fetchContacts()
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (contacts.isEmpty()) {
                    Text(
                        "No Contacts Found",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(contacts) { contact ->
                            ContactListItem(contact = contact, viewModel = viewModel)
                        }
                    }
                }
            } else {
                PermissionPlaceholder(
                    title = "Contacts Access Required",
                    onAllowClick = { launcher.launch(contactPerm) }
                )
            }
        }
    }
}

@Composable
fun ContactListItem(contact: Contact, viewModel: ContactsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.makeCall(contact.phone) }
            .background(Color.White) // Clean background as per screenshot theme
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
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Contact Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Mobile • ${contact.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
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