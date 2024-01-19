package de.inovex.finder.ui.mobile

import android.location.Location
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.inovex.finder.domain.model.POI

@Composable
fun MobileNav(
    onItemClicked: (poi: POI) -> Unit,
    permissionsGranted: Boolean,
    getLocation: (block: (Location?) -> Unit) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.ListScreen.route) {
        composable(Screen.ListScreen.route) {
            POIListComposable(onItemClicked, getLocation, permissionsGranted)
        }
    }
}

sealed class Screen(val route: String) {
    data object ListScreen : Screen("list_screen")
}