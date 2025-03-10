package com.netanel.xplore.quiz.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizTimerManager(private var totalTime: Long) {

    private val _totalTimeLeft = MutableStateFlow(totalTime)
    val totalTimeLeft = _totalTimeLeft.asStateFlow()

    private val _answerLockTimeLeft = MutableStateFlow(0L)
    val answerLockTimeLeft = _answerLockTimeLeft.asStateFlow()

    private var quizTimerJob: Job? = null
    private var answerLockTimerJob: Job? = null
    private var onQuizFinishedListener: (() -> Unit)? = null

    var isAnswerLocked = false
        private set
    var isQuizTimerPaused = false
        private set

    fun setOnQuizFinishedListener(listener: () -> Unit) {
        onQuizFinishedListener = listener
    }

    fun startQuizTimer() {
        quizTimerJob?.cancel()
        quizTimerJob = CoroutineScope(Dispatchers.Main).launch {
            while (_totalTimeLeft.value > 0) {
                if (!isQuizTimerPaused) {
                    delay(1000)
                    _totalTimeLeft.value -= 1000
                } else {
                    delay(100)
                }
            }
            onQuizFinishedListener?.invoke()
        }
    }

    fun stopQuizTimer() {
        quizTimerJob?.cancel()
    }

    fun resetQuizTimer() {
        stopQuizTimer()
        _totalTimeLeft.value = totalTime
    }

    fun updateTotalTime(newTotalTime: Long) {
        totalTime = newTotalTime
        _totalTimeLeft.value = newTotalTime
    }

    fun startAnswerLockTimer(duration: Long, onLockEnd: () -> Unit) {
        answerLockTimerJob?.cancel()
        isAnswerLocked = true  // Lock answers initially
        isQuizTimerPaused = true
        _answerLockTimeLeft.value = duration

        answerLockTimerJob = CoroutineScope(Dispatchers.Main).launch {
            while (_answerLockTimeLeft.value > 0) {
                delay(1000)
                _answerLockTimeLeft.value -= 1000
            }
            isAnswerLocked = false  // Unlock answers after timer ends
            isQuizTimerPaused = false
            onLockEnd()
        }
    }

    fun stopAnswerLockTimer() {
        answerLockTimerJob?.cancel()
        isAnswerLocked = false
        isQuizTimerPaused = false
    }

    fun resetAnswerLock() {
        answerLockTimerJob?.cancel()
        _answerLockTimeLeft.value = 0
        isAnswerLocked = false
        isQuizTimerPaused = false
    }

    fun resetTimers() {
        resetQuizTimer()
        resetAnswerLock()
    }
}
