package ru.zhiev.githubviewer.domain.models

import com.google.gson.annotations.SerializedName

data class GitHubRepositoryModel (
    val id: Int,
    val name: String,
    @SerializedName("private")
    val isPrivate: Boolean,
    val language: String
)