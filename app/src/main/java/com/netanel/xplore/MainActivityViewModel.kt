package com.netanel.xplore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.xplore.utils.SharedPrefKeys
import com.netanel.xplore.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    // Expose login state
    private val _isUserLoggedInFlow = MutableStateFlow(false)
    val isUserLoggedInFlow: StateFlow<Boolean> = _isUserLoggedInFlow

    // Expose quizId state
    private val _selectedQuizId = MutableStateFlow("")
    val selectedQuizId: StateFlow<String> = _selectedQuizId

    init {
        // Initialize states from SharedPreferences
        viewModelScope.launch {
            _isUserLoggedInFlow.value = sharedPreferencesManager.getBoolean(SharedPrefKeys.IS_LOGGED_IN)
            _selectedQuizId.value = sharedPreferencesManager.getString(SharedPrefKeys.QUIZ_ID).orEmpty()
        }
    }

    fun updateUserLoginStatus(isLoggedIn: Boolean) {
        viewModelScope.launch {
            sharedPreferencesManager.saveBoolean(SharedPrefKeys.IS_LOGGED_IN, isLoggedIn)
            _isUserLoggedInFlow.value = isLoggedIn
        }
    }

    fun updateQuizId(quizId: String) {
        viewModelScope.launch {
            sharedPreferencesManager.saveString(SharedPrefKeys.QUIZ_ID, quizId)
            _selectedQuizId.value = quizId
        }
    }
}