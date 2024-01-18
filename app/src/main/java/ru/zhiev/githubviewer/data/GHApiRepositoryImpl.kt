package ru.zhiev.githubviewer.data

import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.models.TokenModel
import ru.zhiev.githubviewer.domain.models.UserModel

class GHApiRepositoryImpl(private val gitHubAPIService: GitHubAPIService) : RepositoryModel {

    override suspend fun getRepositories(token: String): List<GitHubRepositoryModel> {
        return gitHubAPIService.getRepositories(token)
    }

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): TokenModel {
        return gitHubAPIService.getAccessToken(clientId, clientSecret, code)
    }

    override suspend fun getUserData(token: String): UserModel {
        return gitHubAPIService.getUserData(token)
    }
}