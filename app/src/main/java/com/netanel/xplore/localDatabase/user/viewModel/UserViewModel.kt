package com.netanel.xplore.localDatabase.user.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.xplore.auth.repository.model.User
import com.netanel.xplore.localDatabase.user.converters.toEntity
import com.netanel.xplore.localDatabase.user.dao.UserDao
import com.netanel.xplore.localDatabase.user.model.UserEntity
import com.netanel.xplore.utils.SharedPrefKeys
import com.netanel.xplore.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _userFlow = MutableStateFlow<UserEntity?>(null)
    val userFlow: StateFlow<UserEntity?> = _userFlow

    fun loadUserFromStorage() {
        viewModelScope.launch {
            val userId = sharedPreferencesManager.getString(SharedPrefKeys.USER_ID)
            if (!userId.isNullOrEmpty()) {
                _userFlow.value = userDao.getUserById(userId)
            }
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user.toEntity())
            sharedPreferencesManager.saveString(SharedPrefKeys.USER_ID, user.id.toString())
        }
    }

    fun logout() {
        viewModelScope.launch {
            _userFlow.value?.let { userDao.deleteUser(it) }
            sharedPreferencesManager.clearSession()
            _userFlow.value = null
        }
    }
}
