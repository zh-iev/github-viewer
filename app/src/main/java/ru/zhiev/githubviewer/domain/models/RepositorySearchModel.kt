package ru.zhiev.githubviewer.domain.models

data class RepositorySearchModel (
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<GitHubRepositoryModel>
)