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
import com.netanel.xplore.ui.SplashScreen

@Composable
fun NavigationStack(
    userViewModel: UserViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val user by userViewModel.userFlow.collectAsState(initial = null)
    val isLoading by userViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadUserFromStorage()
    }

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        /** 🔹 Splash Screen */
        composable(Screen.SplashScreen.route) {
            SplashScreen(isLoggedIn = user != null)

            LaunchedEffect(isLoading, user) {
                if (!isLoading) {
                    if (user != null) {
                        navController.navigate("${Screen.HomeScreen.route}/${user!!.id}") {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.AuthScreen.route) {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
                        }
                    }
                }
            }
        }

        /** 🔹 Auth Screen */
        composable(route = Screen.AuthScreen.route) {
            AuthScreen(
                userViewModel = userViewModel,
                onLoginSuccess = { userId ->
                    userViewModel.loadUserFromStorage()
                }
            )
        }

        /** 🔹 Home Screen */
        composable(
            route = "${Screen.HomeScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            // 🔄 React to login state
            LaunchedEffect(user) {
                if (user == null) {
                    navController.navigate(Screen.AuthScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                }
            }

            HomeScreen(
                userId = userId,
                userViewModel = userViewModel,
                onQuizSelected = { quizId ->
                    navController.navigate("${Screen.QuizScreen.route}/$userId/$quizId")
                },
                onLogoutClicked = { userViewModel.logout() }
            )
        }

        /** 🔹 Quiz Screen */
        composable(
            route = "${Screen.QuizScreen.route}/{userId}/{quizId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("quizId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            QuizScreen(
                userId = backStackEntry.arguments?.getString("userId") ?: "",
                quizId = backStackEntry.arguments?.getString("quizId") ?: ""
            )
        }
    }

    // 🔹 Ensure Login Navigation Works Immediately
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("${Screen.HomeScreen.route}/${user!!.id}") {
                popUpTo(Screen.AuthScreen.route) { inclusive = true }
            }
        }
    }
}
