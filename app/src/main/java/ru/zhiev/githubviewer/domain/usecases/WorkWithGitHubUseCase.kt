package ru.zhiev.githubviewer.domain.usecases

import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.models.IssueModel
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.models.RepositorySearchModel
import ru.zhiev.githubviewer.domain.models.TokenModel
import ru.zhiev.githubviewer.domain.models.UserModel
import ru.zhiev.githubviewer.domain.models.UsersSearchModel

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

    suspend fun searchRepositories(token: String, query: String): RepositorySearchModel {
        return appRepository.searchRepositories(token, query)
    }
    suspend fun searchUsers(token: String, query: String) : UsersSearchModel {
        return appRepository.searchUsers(token, query)
    }

    suspend fun getIssues(token: String) : List<IssueModel> {
        return appRepository.getIssues(token)
    }

    suspend fun createRepository(token: String, repository: GitHubRepositoryModel) {
        return appRepository.createRepository(token, repository)
    }
}