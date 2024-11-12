package com.netanel.xplore.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.netanel.xplore.quiz.model.Question
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

    // StateFlow to hold the list of questions
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> get() = _questions

    // StateFlow to handle loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // StateFlow to handle error messages
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _isLoading.value = true // Start loading
            try {
                val result = repository.getQuestions()
                _questions.value = result
                _error.value = null // Reset error state
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Failed to load questions: ${e.message}" // Update error state
            } finally {
                _isLoading.value = false // End loading
            }
        }
    }
}