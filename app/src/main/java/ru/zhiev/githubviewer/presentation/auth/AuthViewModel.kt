package ru.zhiev.githubviewer.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.zhiev.githubviewer.Constants
import ru.zhiev.githubviewer.domain.models.TokenModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase

class AuthViewModel(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase
) : ViewModel() {
    private val _accessToken = MutableLiveData<TokenModel>()
    val accessToken: LiveData<TokenModel>
        get() = _accessToken

    fun getAccessToken(code: String) {
        viewModelScope.launch {
            try {
                _accessToken.value = workWithGitHubUseCase.getAccessToken(Constants.CLIENT_ID, Constants.CLIENT_SECRET, code)
                Log.d("OAUTH", "AccessToken: ${_accessToken.value?.accessToken}")
            } catch (e: Exception) {
                Log.d("OAUTH", "getAccessToken: error $e")
            }
        }
    }
}
class AuthViewModelFactory(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(workWithGitHubUseCase) as T
    }
}