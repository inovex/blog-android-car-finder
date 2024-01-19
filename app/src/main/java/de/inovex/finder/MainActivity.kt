package de.inovex.finder

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import de.inovex.finder.data.getLocation
import de.inovex.finder.domain.model.POI
import de.inovex.finder.ui.AppTheme
import de.inovex.finder.ui.mobile.MobileNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var permissionsGranted by mutableStateOf(false)
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
                permissionsGranted = perms.all { it.value }
            }

        setContent {
            AppTheme {
                LaunchedEffect(true) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MobileNav(
                        onItemClicked = { poi ->
                            openMaps(poi)
                        },
                        getLocation = {
                            getLocation(this, it)
                        },
                        permissionsGranted = permissionsGranted
                    )
                }

            }
        }
    }

    private fun openMaps(poi: POI) {
        val uri = Uri.parse("geo:${poi.long},${poi.lat}?q=" + Uri.encode(poi.name))
        Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
            resolveActivity(packageManager)?.let {
                startActivity(this)
            }
        }
    }
}
