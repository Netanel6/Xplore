package com.netanel.xplore.quiz.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class QuizTimerManager(
    private val totalQuizTime: Int,
    private val answerLockDuration: Int = 10
) {
    private var quizTimerJob: Job? = null
    private var answerLockJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _totalTimeLeft = MutableStateFlow(totalQuizTime)
    val totalTimeLeft: StateFlow<Int> = _totalTimeLeft

    private val _answerLockTimeLeft = MutableStateFlow(answerLockDuration)
    val answerLockTimeLeft: StateFlow<Int> = _answerLockTimeLeft

    private val answerLockCompletion = mutableMapOf<Int, Boolean>()

    var isQuizTimerPaused = false
        private set

    var isAnswerLocked = true
        private set

    fun startQuizTimer(onFinish: () -> Unit) {
        quizTimerJob?.cancel()
        quizTimerJob = coroutineScope.launch {
            while (_totalTimeLeft.value > 0 && isActive) {
                if (!isQuizTimerPaused) {
                    delay(1000L)
                    _totalTimeLeft.value -= 1
                }
            }
            onFinish()
        }
    }

    fun startAnswerLockTimer(questionIndex: Int, onUnlock: () -> Unit) {
        if (answerLockCompletion[questionIndex] == true) {
            isAnswerLocked = false
            _answerLockTimeLeft.value = 0
            return
        }

        isAnswerLocked = true
        isQuizTimerPaused = true
        _answerLockTimeLeft.value = answerLockDuration

        answerLockJob?.cancel()
        answerLockJob = coroutineScope.launch {
            while (_answerLockTimeLeft.value > 0 && isActive) {
                delay(1000L)
                _answerLockTimeLeft.value -= 1
            }
            isAnswerLocked = false
            isQuizTimerPaused = false
            answerLockCompletion[questionIndex] = true
            onUnlock()
        }
    }

    fun resetQuizTimer() {
        quizTimerJob?.cancel()
        _totalTimeLeft.value = totalQuizTime
        isQuizTimerPaused = false
    }

    fun resetAnswerLock() {
        answerLockJob?.cancel()
        answerLockCompletion.clear()
        _answerLockTimeLeft.value = answerLockDuration
        isAnswerLocked = true
    }

    fun stopAllTimers() {
        quizTimerJob?.cancel()
        answerLockJob?.cancel()
    }
}
