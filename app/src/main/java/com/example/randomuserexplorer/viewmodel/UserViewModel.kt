package com.example.randomuserexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserexplorer.data.model.User
import com.example.randomuserexplorer.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var totalUsersToFetch = 0
    private var totalFetchedUsers = 0
    private val pageSize = 10
    private var currentPage = 1

    fun loadUsers(userInputSize: Int) {
        if (_isLoading.value || userInputSize <= 0) return

        totalUsersToFetch = userInputSize
        totalFetchedUsers = 0
        currentPage = 1
        _userList.value = emptyList()
        _errorMessage.value = null

        fetchNextPage()
    }

    fun fetchNextPage() {
        if (_isLoading.value || totalFetchedUsers >= totalUsersToFetch) return

        viewModelScope.launch {
            repository.fetchUsers(pageSize)
                .onStart { _isLoading.value = true }
                .catch { e ->

                    _errorMessage.value = e.message ?: "Something went wrong"

                }
                .collect { users ->
                    if (users.isEmpty()) {
                        _errorMessage.value = "No more users available."
                        return@collect
                    }

                    val remainingUsers = totalUsersToFetch - totalFetchedUsers
                    val limitedUsers = users.take(remainingUsers)

                    _userList.update { it + limitedUsers }
                    totalFetchedUsers += limitedUsers.size
                    currentPage++
                }

            _isLoading.value = false
        }
    }
}
