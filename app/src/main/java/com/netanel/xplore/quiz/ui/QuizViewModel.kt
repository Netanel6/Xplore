package com.netanel.xplore.quiz.ui

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
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _quizState = MutableStateFlow<QuizState>(QuizState.Loading)
    val quizState: StateFlow<QuizState> get() = _quizState

    private var currentQuiz: Quiz? = null
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> get() = _currentQuestionIndex

    private val _showAnimation = MutableStateFlow(false)
    val showAnimation: StateFlow<Boolean> get() = _showAnimation

    private val _animationData = MutableStateFlow(AnimationData(0, false, ""))
    val animationData: StateFlow<AnimationData> get() = _animationData

    fun loadQuiz(quizId: String) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            try {
                val loadedQuiz = repository.getQuiz(quizId).copy(totalScore = 0)
                currentQuiz = loadedQuiz
                _quizState.value = QuizState.Loaded(currentQuiz!!)
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to load quiz: ${e.message}")
            }
        }
    }

    fun selectAnswer(answerIndex: Int) {
        currentQuiz?.let { quiz ->
            val questions = quiz.questions.toMutableList()
            val question = questions[_currentQuestionIndex.value]

            if (!question.isAnswered) {
                questions[_currentQuestionIndex.value] =
                    question.copy(userSelectedAnswer = answerIndex)
                currentQuiz = quiz.copy(questions = questions)
                _quizState.value = QuizState.Loaded(currentQuiz!!)
            }
        }
    }

    fun lockAnswer() {
        currentQuiz?.let { quiz ->
            val questions = quiz.questions.toMutableList()
            val question = questions[_currentQuestionIndex.value]

            if (!question.isAnswered && question.userSelectedAnswer != null) {
                val isCorrect = question.userSelectedAnswer == question.correctAnswerIndex
                val pointsGained = if (isCorrect) question.pointsGained else 0

                questions[_currentQuestionIndex.value] = question.copy(
                    isAnswered = true,
                    pointsGained = pointsGained,
                    isCorrect = isCorrect
                )
                currentQuiz = quiz.copy(
                    questions = questions,
                    totalScore = quiz.totalScore + pointsGained
                )

                // 🎉 Show animation before proceeding
                _animationData.value = AnimationData(
                    pointsGained,
                    isCorrect,
                    question.answers?.get(question.correctAnswerIndex ?: 0) ?: ""
                )
                _showAnimation.value = true

                _quizState.value = QuizState.Loaded(currentQuiz!!)
            }
        }
    }

    fun hideAnimation() {
        _showAnimation.value = false
        nextQuestion()
    }

    fun nextQuestion() {
        val quizSize = currentQuiz?.questions?.size ?: 1
        if (_currentQuestionIndex.value < quizSize - 1) {
            _currentQuestionIndex.value += 1
            _quizState.value = QuizState.Loaded(currentQuiz!!)
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
            _quizState.value = QuizState.Loaded(currentQuiz!!)
        }
    }

    fun resetQuiz() {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            try {
                val quizId = currentQuiz?._id ?: return@launch
                val freshQuiz = repository.getQuiz(quizId).copy(
                    totalScore = 0,
                    questions = repository.getQuiz(quizId).questions.map { it.copy(
                        userSelectedAnswer = null,
                        isAnswered = false,
                        pointsGained = 0
                    ) }
                )

                currentQuiz = freshQuiz
                _currentQuestionIndex.value = 0
                _quizState.value = QuizState.Loaded(freshQuiz)
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to reset quiz: ${e.message}")
            }
        }
    }
}

data class AnimationData(
    val points: Int,
    val isCorrect: Boolean,
    val correctAnswer: String
)
