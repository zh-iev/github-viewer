package ru.zhiev.githubviewer.domain.models

data class IssueModel (
    val title: String,
    val body: String? = "",
    val repository: GitHubRepositoryModel,
    val state: String? = ""
)