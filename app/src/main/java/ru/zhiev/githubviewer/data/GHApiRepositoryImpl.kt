package ru.zhiev.githubviewer.data

import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.models.OwnerModel
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.models.TokenModel
import ru.zhiev.githubviewer.domain.models.UserModel

class GHApiRepositoryImpl(private val gitHubAPIService: GitHubAPIService) : RepositoryModel {

    override suspend fun getRepositories(token: String): List<GitHubRepositoryModel> {
        return gitHubAPIService.getRepositories(token).map {
            GitHubRepositoryModel(
                id = it.id,
                name = it.name,
                isPrivate = it.isPrivate,
                description = it.description,
                language = it.language,
                pushedAt = it.pushedAt,
                owner = OwnerModel(
                    login = it.owner.login,
                    id = it.owner.id
                )
            )
        }
    }

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): TokenModel {
        return TokenModel(
            accessToken = gitHubAPIService.getAccessToken(clientId, clientSecret, code).accessToken
        )
    }

    override suspend fun getUserData(token: String): UserModel {
        return gitHubAPIService.getUserData(token).let {
            UserModel(
                login = it.login,
                name = it.name,
                email = it.email,
                bio = it.bio,
                avatarUrl = it.avatarUrl
            )
        }
    }
}