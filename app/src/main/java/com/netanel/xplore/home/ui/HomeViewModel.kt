package com.netanel.xplore.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _quizzes = MutableStateFlow<List<Quiz>?>(null)
    val quizList: StateFlow<List<Quiz>?> = _quizzes

    private val _selectedQuiz = MutableStateFlow<Quiz?>(null)
    val selectedQuiz: StateFlow<Quiz?> = _selectedQuiz

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun setSelectedQuiz(quiz: Quiz) {
        _selectedQuiz.value = quiz
    }

    fun fetchUserQuizzes(userId: String) {
        viewModelScope.launch {
            try {
                val quizzes = quizRepository.getQuizListForUser(userId)
                _quizzes.value = quizzes
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}



