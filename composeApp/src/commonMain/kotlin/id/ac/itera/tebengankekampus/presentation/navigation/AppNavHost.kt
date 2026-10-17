package id.ac.itera.tebengankekampus.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import id.ac.itera.tebengankekampus.presentation.screens.detail.DetailScreen
import id.ac.itera.tebengankekampus.presentation.screens.home.HomeScreen
import id.ac.itera.tebengankekampus.presentation.screens.pengaturan.PengaturanScreen
import id.ac.itera.tebengankekampus.presentation.screens.peta.PetaScreen
import id.ac.itera.tebengankekampus.presentation.screens.profil.ProfilScreen
import id.ac.itera.tebengankekampus.presentation.screens.tambah.TambahTebenganScreen

@Composable
fun AppNavHost(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route: String? = backStackEntry?.destination?.route
    val ruteTab = listOf(Routes.BERANDA, Routes.PETA, Routes.PROFIL)

    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.BERANDA,
            modifier = Modifier.weight(1f),
        ) {
            composable(Routes.BERANDA) {
                HomeScreen(
                    onLihatDetail = { id -> navController.navigate(Routes.detail(id)) },
                    onTawarkan = { navController.navigate(Routes.TAMBAH) },
                )
            }
            composable(Routes.TAMBAH) {
                TambahTebenganScreen(onSelesai = { navController.popBackStack() })
            }
            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                DetailScreen(
                    id = entry.arguments?.read { getStringOrNull("id") }.orEmpty(),
                    onKembali = { navController.popBackStack() },
                )
            }
            composable(Routes.PETA) { PetaScreen() }
            composable(Routes.PROFIL) {
                ProfilScreen(onPengaturan = { navController.navigate(Routes.PENGATURAN) })
            }
            composable(Routes.PENGATURAN) {
                PengaturanScreen(
                    darkMode = darkMode,
                    onToggleDarkMode = onToggleDarkMode,
                    onKembali = { navController.popBackStack() },
                )
            }
        }

        if (route in ruteTab) {
            NavigationBar {
                NavigationBarItem(
                    selected = route == Routes.BERANDA,
                    onClick = { navController.navigateKeTab(Routes.BERANDA) },
                    icon = { Text("🏠") },
                    label = { Text("Beranda") },
                )
                NavigationBarItem(
                    selected = route == Routes.PETA,
                    onClick = { navController.navigateKeTab(Routes.PETA) },
                    icon = { Text("🗺️") },
                    label = { Text("Peta") },
                )
                NavigationBarItem(
                    selected = route == Routes.PROFIL,
                    onClick = { navController.navigateKeTab(Routes.PROFIL) },
                    icon = { Text("👤") },
                    label = { Text("Profil") },
                )
            }
        }
    }
}

private fun NavHostController.navigateKeTab(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
