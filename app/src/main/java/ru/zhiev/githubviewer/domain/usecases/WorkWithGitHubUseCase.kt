package ru.zhiev.githubviewer.domain.usecases

import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.models.TokenModel
import ru.zhiev.githubviewer.domain.models.UserModel

class WorkWithGitHubUseCase(private val appRepository: RepositoryModel) {
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
    ): TokenModel {
        return appRepository.getAccessToken(clientId, clientSecret, code)
    }

    suspend fun getRepositories(token: String): List<GitHubRepositoryModel> {
        return appRepository.getRepositories(token)
    }

    suspend fun getUserData(token: String): UserModel {
        return appRepository.getUserData(token)
    }
}