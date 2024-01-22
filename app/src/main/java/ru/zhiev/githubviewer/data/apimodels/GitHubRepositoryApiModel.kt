package ru.zhiev.githubviewer.data.apimodels

import com.google.gson.annotations.SerializedName

data class GitHubRepositoryApiModel (
    val id: Int,
    val name: String,
    @SerializedName("private")
    val isPrivate: Boolean,
    val description: String,
    val language: String,
    @SerializedName("pushed_at")
    val pushedAt: String,
    val owner: UserApiModel
)