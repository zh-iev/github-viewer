package ru.zhiev.githubviewer.domain.models

data class UsersSearchModel (
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<UserModel>
)