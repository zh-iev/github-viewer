package ru.zhiev.githubviewer.presentation.ui.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase

class RepositoriesViewModel (private val workWithGitHubUseCase: WorkWithGitHubUseCase) : ViewModel() {

    private val _repositoties = MutableLiveData<List<GitHubRepositoryModel>>()
    val repositories: LiveData<List<GitHubRepositoryModel>> get() =_repositoties
    fun getRepos(token: String) {
        viewModelScope.launch {
            try {
                _repositoties.value = workWithGitHubUseCase.getRepositories("bearer $token")
            }
            catch (e: Exception) {
                Log.d("ER_Repo", "error $e")
            }
        }
    }
}

class RepositoriesViewModelFactory(private val workWithGitHubUseCase: WorkWithGitHubUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RepositoriesViewModelFactory(workWithGitHubUseCase) as T
    }
}
