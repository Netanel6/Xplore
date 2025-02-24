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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferencesManager: SharedPreferencesManager
): ViewModel() {

    private val _userFlow = MutableStateFlow<UserEntity?>(null)
    val userFlow: StateFlow<UserEntity?> = _userFlow

    val username: Flow<String> = _userFlow.map { it?.name?: "" }

    fun loadUserFromStorage() {
        viewModelScope.launch {
            try {
                val userId = sharedPreferencesManager.getString(SharedPrefKeys.USER_ID)
                if (userId.isNotEmpty()) {
                    _userFlow.value = userDao.getUserById(userId)
                }
            } catch (e: Exception) {
                //TODO: Handle the exception, e.g., log the error or emit an error state
            }
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            try {
                userDao.insertUser(user.toEntity())
                sharedPreferencesManager.saveString(SharedPrefKeys.USER_ID, user.id.toString())
            } catch (e: Exception) {
                //TODO: Handle the exception, e.g., log the error or emit an error state
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _userFlow.value?.let { userDao.deleteUser(it) }
                sharedPreferencesManager.clearSession()
                _userFlow.value = null
            } catch (e: Exception) {
                //TODO: Handle the exception, e.g., log the error or emit an error state
            }
        }
    }
}