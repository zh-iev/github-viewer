package ru.zhiev.githubviewer.domain.models

interface RepositoryModel {
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
    ) : TokenModel

    suspend fun getUserData(token: String) : UserModel
    suspend fun getRepositories(token: String) : List<GitHubRepositoryModel>

    suspend fun searchRepositories(token: String, query: String) : RepositorySearchModel
}