package com.netanel.xplore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.netanel.xplore.auth.ui.AuthScreen
import com.netanel.xplore.home.HomeScreen
import com.netanel.xplore.localDatabase.user.viewModel.UserViewModel
import com.netanel.xplore.quiz.ui.QuizScreen

@Composable
fun NavigationStack(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val user by userViewModel.userFlow.collectAsState(initial = null)
    val isLoggedIn = user != null

    // Load user session when the app starts
    LaunchedEffect(Unit) {
        userViewModel.loadUserFromStorage()
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "${Screen.HomeScreen.route}/{userId}" else Screen.AuthScreen.route
    ) {
        /** 🔹 Auth Screen */
        composable(route = Screen.AuthScreen.route) {
            AuthScreen(
                userViewModel = userViewModel,
                onLoginSuccess = { userId ->
                    navController.navigate("${Screen.HomeScreen.route}/$userId") {
                        popUpTo(Screen.AuthScreen.route) { inclusive = true }
                    }
                }
            )
        }

        /** 🔹 Home Screen */
        composable(
            route = "${Screen.HomeScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: user?.id ?: ""

            if (userId.isNotEmpty()) {
                HomeScreen(
                    userId = userId,
                    onQuizSelected = { quizId ->
                        navController.navigate("${Screen.QuizScreen.route}/$userId/$quizId")
                    },
                    onLogoutClicked = {
                        navController.navigate(Screen.AuthScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            } else {
                navController.navigate(Screen.AuthScreen.route) {
                    popUpTo(Screen.HomeScreen.route) { inclusive = true }
                }
            }
        }

        /** 🔹 Quiz Screen */
        composable(
            route = "${Screen.QuizScreen.route}/{userId}/{quizId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("quizId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
            QuizScreen(userId = userId, quizId = quizId)
        }
    }
}
