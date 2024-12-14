package com.netanel.xplore.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.xplore.home.viewmodel.HomeViewModel
import com.netanel.xplore.quiz.ui.composables.LoadingScreen

@Composable
fun HomeScreen(
    onQuizSelected: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val quiz by viewModel.quiz.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    if (isLoading) {
        LoadingScreen()
        return
    }

    if (errorMessage != null) {
        /* ErrorScreen(
             message = errorMessage,
             onRetry = { viewModel.fetchQuizzes() }
         )*/
        return
    }

    if (quiz != null) {
        Text(quiz.toString())
    }
}
