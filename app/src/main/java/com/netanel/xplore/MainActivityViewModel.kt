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

    // Expose the login state as StateFlow
    private val _isUserLoggedInFlow = MutableStateFlow(sharedPreferencesManager.getBoolean(SharedPrefKeys.IS_LOGGED_IN))
    val isUserLoggedInFlow: StateFlow<Boolean> = _isUserLoggedInFlow

    // Function to update the login state
    fun updateUserLoginStatus(isLoggedIn: Boolean) {
        viewModelScope.launch {
            sharedPreferencesManager.saveBoolean(SharedPrefKeys.IS_LOGGED_IN, isLoggedIn)
            _isUserLoggedInFlow.value = isLoggedIn // Update the StateFlow to reflect the change
        }
    }
}