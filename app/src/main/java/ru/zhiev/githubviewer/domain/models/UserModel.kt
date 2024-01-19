package ru.zhiev.githubviewer.domain.models

import com.google.gson.annotations.SerializedName

data class UserModel (
    val login: String,
    val name: String,
    val email: String,
    val bio: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
)