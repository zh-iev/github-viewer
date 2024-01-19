package ru.zhiev.githubviewer

import android.app.Application
import ru.zhiev.githubviewer.data.GHApiRepositoryImpl
import ru.zhiev.githubviewer.data.GitHubAPIRetrofit

class GitHubViewerApplication : Application() {
    private var _repository: GHApiRepositoryImpl? = null

    val repository: GHApiRepositoryImpl
        get() = _repository!!

    override fun onCreate() {
        super.onCreate()
        val gitHubAPIService = GitHubAPIRetrofit.gitHubAPIService
        _repository = GHApiRepositoryImpl(gitHubAPIService)
    }
}