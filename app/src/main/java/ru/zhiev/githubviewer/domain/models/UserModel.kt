package ru.zhiev.githubviewer.domain.models

data class UserModel (
    val login: String,
    val name: String,
    val email: String,
    val description: String,
    val location: String,
)