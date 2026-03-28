import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel

data class PermissionState(
    val grantedPermissions: List<String> = emptyList(),
    val deniedPermissions: List<String> = emptyList()
)

class PermissionViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    var permissionState by mutableStateOf(PermissionState())
        private set

    fun checkPermissions(permissions: Array<String>) {
        val granted = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        val denied = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        permissionState = PermissionState(granted, denied)
    }

    fun updatePermissionState(result: Map<String, Boolean>) {
        val currentGranted = permissionState.grantedPermissions.toMutableSet()
        val currentDenied = permissionState.deniedPermissions.toMutableSet()

        result.forEach { (permission, isGranted) ->
            if (isGranted) {
                currentGranted.add(permission)
                currentDenied.remove(permission)
            } else {
                currentDenied.add(permission)
                currentGranted.remove(permission)
            }
        }

        permissionState = PermissionState(
            grantedPermissions = currentGranted.toList(),
            deniedPermissions = currentDenied.toList()
        )
    }


}