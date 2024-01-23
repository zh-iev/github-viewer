package ru.zhiev.githubviewer.data.apimodels

import com.google.gson.annotations.SerializedName

data class RepositorySearchApiModel (
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<GitHubRepositoryApiModel>
)