package ru.zhiev.githubviewer.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.zhiev.githubviewer.domain.models.UserModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase
import java.lang.Exception

class MainViewModel(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase
) : ViewModel() {

    private val _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel>
        get() = _userData

    var wasWelcome: Boolean = false

    fun getUserData(token: String){
        viewModelScope.launch {
            try {
                _userData.value = workWithGitHubUseCase.getUserData("bearer $token")
                Log.d("GETUSERDATA", "getUserData: ${_userData.value}")
            } catch (e: Exception) {
                Log.d("ER_UserData", "getUserData: error $e")
            }
        }
    }
}
class MainViewModelFactory(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(workWithGitHubUseCase) as T
    }
}