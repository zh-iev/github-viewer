package ru.zhiev.githubviewer.data.apimodels

data class IssueApiModel (
    val title: String,
    val body: String? = "",
    val repository: GitHubRepositoryApiModel,
    val state: String? = ""
)
