package ru.zhiev.githubviewer.data.apimodels

import com.google.gson.annotations.SerializedName

data class UserApiModel (
    val login: String,
    val name: String,
    val email: String,
    val bio: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
)