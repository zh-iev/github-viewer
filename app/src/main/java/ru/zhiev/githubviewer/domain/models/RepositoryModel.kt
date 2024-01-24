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

    suspend fun searchUsers(token: String, query: String) : UsersSearchModel

    suspend fun getIssues(token: String) : List<IssueModel>

    suspend fun createRepository(token: String, repository: GitHubRepositoryModel)

    suspend fun createIssue(token: String, owner: UserModel, issue: IssueModel)
}