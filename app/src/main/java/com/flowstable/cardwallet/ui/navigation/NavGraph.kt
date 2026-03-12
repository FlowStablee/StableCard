package com.flowstable.cardwallet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flowstable.cardwallet.ui.screens.CardDetailScreen
import com.flowstable.cardwallet.ui.screens.DashboardScreen
import com.flowstable.cardwallet.ui.screens.FreezeCardScreen
import com.flowstable.cardwallet.ui.screens.LoginScreen
import com.flowstable.cardwallet.ui.screens.ProfileScreen
import com.flowstable.cardwallet.ui.screens.SettingsScreen
import com.flowstable.cardwallet.ui.screens.SplashScreen
import com.flowstable.cardwallet.ui.screens.TransactionsScreen
import com.flowstable.cardwallet.viewmodel.SessionViewModel

object Destinations {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val CARD_DETAIL = "card_detail"
    const val FREEZE_CARD = "freeze_card"
    const val TRANSACTIONS = "transactions"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
}

@Composable
fun FlowstableNavHost(
    sessionViewModel: SessionViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val sessionState by sessionViewModel.sessionState

    LaunchedEffect(sessionState.isAuthenticated) {
        if (sessionState.checkedInitialSession) {
            if (sessionState.isAuthenticated) {
                navController.navigate(Destinations.DASHBOARD) {
                    popUpTo(Destinations.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(Destinations.LOGIN) {
                    popUpTo(Destinations.SPLASH) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destinations.SPLASH
    ) {
        composable(Destinations.SPLASH) {
            SplashScreen()
        }
        composable(Destinations.LOGIN) {
            val authViewModel = hiltViewModel<com.flowstable.cardwallet.viewmodel.AuthViewModel>()
            LoginScreen(
                state = authViewModel.uiState,
                onLogin = { email, password -> authViewModel.login(email, password) },
                onBiometricLogin = { authViewModel.biometricLogin() }
            )
        }
        composable(Destinations.DASHBOARD) {
            val dashboardViewModel =
                hiltViewModel<com.flowstable.cardwallet.viewmodel.DashboardViewModel>()
            DashboardScreen(
                state = dashboardViewModel.uiState,
                onCardSelected = { cardId ->
                    navController.navigate("${Destinations.CARD_DETAIL}/$cardId")
                },
                onViewTransactions = {
                    navController.navigate(Destinations.TRANSACTIONS)
                },
                onProfileClick = { navController.navigate(Destinations.PROFILE) },
                onSettingsClick = { navController.navigate(Destinations.SETTINGS) }
            )
        }
        composable("${Destinations.CARD_DETAIL}/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
            val cardViewModel =
                hiltViewModel<com.flowstable.cardwallet.viewmodel.CardViewModel>()
            CardDetailScreen(
                cardId = cardId,
                state = cardViewModel.uiState,
                onFreezeClick = { navController.navigate("${Destinations.FREEZE_CARD}/$cardId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("${Destinations.FREEZE_CARD}/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
            val cardViewModel =
                hiltViewModel<com.flowstable.cardwallet.viewmodel.CardViewModel>()
            FreezeCardScreen(
                cardId = cardId,
                state = cardViewModel.uiState,
                onConfirm = { limit, online, international ->
                    cardViewModel.updateControls(cardId, limit, online, international)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.TRANSACTIONS) {
            val txViewModel =
                hiltViewModel<com.flowstable.cardwallet.viewmodel.TransactionsViewModel>()
            TransactionsScreen(
                state = txViewModel.uiState,
                onFilterChanged = { from, to, min, max ->
                    txViewModel.applyFilter(from, to, min, max)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.PROFILE) {
            val profileViewModel =
                hiltViewModel<com.flowstable.cardwallet.viewmodel.ProfileViewModel>()
            ProfileScreen(
                state = profileViewModel.uiState,
                onLogout = { profileViewModel.logout() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.SETTINGS) {
            val settingsViewModel =
                hiltViewModel<com.flowstable.cardwallet.viewmodel.SettingsViewModel>()
            SettingsScreen(
                state = settingsViewModel.uiState,
                onToggleDarkMode = { settingsViewModel.toggleDarkMode() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

