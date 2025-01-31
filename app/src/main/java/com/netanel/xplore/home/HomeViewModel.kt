package com.netanel.xplore.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _quizzes = MutableStateFlow<List<User.Quiz>?>(null)
    val quizList: StateFlow<List<User.Quiz>?> = _quizzes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchQuizzes(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val quiz = quizRepository.getQuizListForUser(userId)
                _quizzes.value = quiz
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }
}
