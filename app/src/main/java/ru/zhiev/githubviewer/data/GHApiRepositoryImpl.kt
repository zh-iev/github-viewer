package ru.zhiev.githubviewer.data

import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.models.IssueModel
import ru.zhiev.githubviewer.domain.models.OwnerModel
import ru.zhiev.githubviewer.domain.models.RepositoryModel
import ru.zhiev.githubviewer.domain.models.RepositorySearchModel
import ru.zhiev.githubviewer.domain.models.TokenModel
import ru.zhiev.githubviewer.domain.models.UserModel
import ru.zhiev.githubviewer.domain.models.UsersSearchModel

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

    override suspend fun searchRepositories(token: String, query: String): RepositorySearchModel {
        return gitHubAPIService.searchRepositories(token, query).let {
            RepositorySearchModel(
                totalCount = it.totalCount,
                incompleteResults = it.incompleteResults,
                items = it.items.map { repo ->
                    GitHubRepositoryModel(
                        id = repo.id,
                        name = repo.name,
                        isPrivate = repo.isPrivate,
                        description = repo.description,
                        language = repo.language,
                        pushedAt = repo.pushedAt,
                        owner = OwnerModel(
                            login = repo.owner.login,
                            id = repo.owner.id
                        )
                    )
                }
            )
        }
    }

    override suspend fun searchUsers(token: String, query: String): UsersSearchModel {
        return gitHubAPIService.searchUsers(token, query).let {
            UsersSearchModel(
                totalCount = it.totalCount,
                incompleteResults = it.incompleteResults,
                items = it.items.map { user ->
                    UserModel(
                        login = user.login,
                        name = user.name,
                        email = user.email,
                        bio = user.bio,
                        avatarUrl = user.avatarUrl
                    )
                }
            )
        }
    }

    override suspend fun getIssues(token: String): List<IssueModel> {
        return gitHubAPIService.getIssues(token).map {
            IssueModel(
                title = it.title,
                body = it.body,
                repository = it.repository.let {repo ->
                    GitHubRepositoryModel(
                        id = repo.id,
                        name = repo.name,
                        isPrivate = repo.isPrivate,
                        description = repo.description,
                        language = repo.language,
                        pushedAt = repo.pushedAt,
                        owner = OwnerModel(
                            login = repo.owner.login,
                            id = repo.owner.id
                        )
                    )
                }
            )
        }
    }
}