package ru.zhiev.githubviewer.presentation.ui.issues

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.zhiev.githubviewer.domain.models.IssueModel
import ru.zhiev.githubviewer.domain.usecases.WorkWithGitHubUseCase
import java.lang.Exception

class SlideshowViewModel(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase,
) : ViewModel() {
    private val _issues = MutableLiveData<List<IssueModel>>()
    val issues: MutableLiveData<List<IssueModel>>
        get() = _issues

    fun getIssues(token: String) {
        viewModelScope.launch {
            try {
                _issues.value =
                    workWithGitHubUseCase.getIssues("bearer $token")
            } catch (e: Exception) {
                Log.d("ER_Issues", "getIssues: error $e")
            }
        }
    }
}

class SlideshowViewModelFactory(
    private val workWithGitHubUseCase: WorkWithGitHubUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SlideshowViewModel(workWithGitHubUseCase) as T
    }
}