package com.netanel.xplore.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    // StateFlow to hold the quiz
    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz: StateFlow<Quiz?> get() = _quiz

    // StateFlow to hold the list of questions
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> get() = _questions

    // StateFlow to handle loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // StateFlow to handle error messages
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // DataFlow to handle scoring
    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> get() = _totalScore


    fun loadQuiz(quizId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedQuiz = repository.getQuiz(quizId)
                _quiz.value = loadedQuiz
                _questions.value = loadedQuiz.questions
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Failed to load quiz: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun addScore(points: Int) {
        _totalScore.value += points
    }

}