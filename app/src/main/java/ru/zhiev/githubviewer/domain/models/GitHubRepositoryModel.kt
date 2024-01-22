package ru.zhiev.githubviewer.domain.models

data class GitHubRepositoryModel (
    val id: Int,
    val name: String? = "",
    val isPrivate: Boolean,
    val description: String? = "",
    val language: String? = "",
    val pushedAt: String? = "",
    val owner: UserModel
)