package com.netanel.xplore

import androidx.lifecycle.ViewModel
import com.netanel.xplore.utils.SharedPrefKeys
import com.netanel.xplore.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
     return sharedPreferencesManager.getBoolean(SharedPrefKeys.IS_LOGGED_IN)
    }
}