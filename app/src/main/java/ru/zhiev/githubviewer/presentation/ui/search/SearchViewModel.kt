package ru.zhiev.githubviewer.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.zhiev.githubviewer.domain.models.RepositorySearchModel
import ru.zhiev.githubviewer.domain.models.UsersSearchModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase
import java.lang.Exception

class SearchViewModel(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase
) : ViewModel() {
    private val _foundRepositories = MutableLiveData<RepositorySearchModel>()
    val foundRepositories: LiveData<RepositorySearchModel>
        get() = _foundRepositories

    private val _foundUsers = MutableLiveData<UsersSearchModel>()
    val foundUsers: LiveData<UsersSearchModel>
        get() = _foundUsers

    fun searchRepositories(token: String, query: String){
        viewModelScope.launch {
            try {
                _foundRepositories.value = workWithGitHubUseCase.searchRepositories("bearer $token", query)
            } catch (e: Exception) {
                Log.d("ER_Repo", "searchRepositories: error $e")
            }
        }
    }
    fun searchUsers(token: String, query: String) {
        viewModelScope.launch {
            try {
                _foundUsers.value = workWithGitHubUseCase.searchUsers("bearer $token", query)
            } catch (e: Exception) {
                Log.d("ER_Repo", "searchUsers: error $e")
            }
        }
    }
}

class SearchViewModelFactory(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(workWithGitHubUseCase) as T
    }
}