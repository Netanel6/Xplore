package com.netanel.xplore.quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.xplore.quiz.model.Question
import com.netanel.xplore.quiz.model.Quiz
import com.netanel.xplore.quiz.model.UpdateScoreRequest
import com.netanel.xplore.quiz.repository.QuizRepository
import com.netanel.xplore.utils.SharedPrefKeys.USER_ID
import com.netanel.xplore.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _quizState = MutableStateFlow<QuizState>(QuizState.Loading)
    val quizState: StateFlow<QuizState> get() = _quizState

    private var currentQuiz: Quiz? = null
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> get() = _currentQuestionIndex

    private val _quizResult = MutableStateFlow<QuizResult?>(null)
    val quizResult: StateFlow<QuizResult?> get() = _quizResult

    fun loadQuiz(quiz: Quiz) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            try {
                currentQuiz = quiz
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
                val pointsGained = if (isCorrect) question.points else 0

                questions[_currentQuestionIndex.value] = question.copy(
                    isAnswered = true,
                    points = pointsGained,
                    isCorrect = isCorrect
                )
                currentQuiz = quiz.copy(questions = questions)

                // Update quiz result
                val questionResult = QuestionResult(
                    question = question,
                    isCorrect = isCorrect,
                    pointsAwarded = pointsGained
                )
                val updatedResults = _quizResult.value?.questionResults.orEmpty() + questionResult
                val updatedTotalScore = updatedResults.sumOf { it.pointsAwarded }
                _quizResult.value = QuizResult(
                    totalScore = updatedTotalScore,
                    questionResults = updatedResults
                )

                _quizState.value = QuizState.Loaded(currentQuiz!!)
            }
        }
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
                val freshQuiz = quizRepository.getQuiz(quizId).copy(
                    questions = quizRepository.getQuiz(quizId).questions.map {
                        it.copy(
                        userSelectedAnswer = null,
                        isAnswered = false,
                        points = 0
                    ) }
                )

                currentQuiz = freshQuiz
                _currentQuestionIndex.value = 0
                _quizResult.value = null
                _quizState.value = QuizState.Loaded(freshQuiz)
            } catch (e: Exception) {
                _quizState.value = QuizState.Error("Failed to reset quiz: ${e.message}")
            }
        }
    }

    fun updateQuiz() {
        val userId = sharedPreferencesManager.getString(USER_ID, "")
        val updateScoreRequest = UpdateScoreRequest(userId, quizResult.value?.totalScore)
        viewModelScope.launch {
            currentQuiz?._id?.let { quizRepository.updateQuiz(it, updateScoreRequest) }
        }
    }
    fun fetchQuizById(quizId: String) {
        viewModelScope.launch {
            try {
                _quizState.value = QuizState.Loading
                val quiz = quizRepository.getQuiz(quizId)
                _quizState.value = QuizState.Loaded(quiz)
            } catch (e: Exception) {
                _quizState.value = QuizState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

data class AnimationData(
    val points: Int,
    val isCorrect: Boolean,
    val correctAnswer: String
)


data class QuizResult(
    val totalScore: Int,
    val questionResults: List<QuestionResult>
)

data class QuestionResult(
    val question: Question,
    val isCorrect: Boolean,
    val pointsAwarded: Int
)
