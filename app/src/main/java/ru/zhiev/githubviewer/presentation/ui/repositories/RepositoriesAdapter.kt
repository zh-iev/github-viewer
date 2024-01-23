package ru.zhiev.githubviewer.presentation.ui.repositories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RepositoriesAdapter(
    private val context: Context,
    private val dataSet: List<GitHubRepositoryModel>,
    private val onItemClick: (GitHubRepositoryModel) -> Unit)
    : RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_repositories_item, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val repository = dataSet[position]
            viewHolder.nameRepo.text = repository.name
            setVisibilityWithData(viewHolder.descriptionRepo, repository.description)
            setVisibilityWithData(viewHolder.languageRepo, repository.language)

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

            val date: Date = inputFormat.parse(repository.pushedAt)
            val formattedDate: String = outputFormat.format(date)
            setVisibilityWithData(viewHolder.lastUpdatedRepo, "${context.getString(R.string.lastUpd)}: $formattedDate")


            viewHolder.ownerRepo.text = repository.owner!!.login
            Glide.with(viewHolder.avatarRepo)
                .load(repository.owner.avatarUrl)
                .apply(RequestOptions().circleCrop())
                .into(viewHolder.avatarRepo)

            if (repository.isPrivate) viewHolder.isPrivateRepos.visibility = View.VISIBLE

            viewHolder.itemView.setOnClickListener {
                onItemClick(repository)
            }
        }
        override fun getItemCount() = dataSet.size
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val avatarRepo: ImageView
            val nameRepo: TextView
            val descriptionRepo: TextView
            val languageRepo: Chip
            val lastUpdatedRepo: TextView
            val ownerRepo: TextView
            val isPrivateRepos: ImageView

            init {
                avatarRepo = view.findViewById(R.id.avatarRepo)
                nameRepo = view.findViewById(R.id.nameRepo)
                descriptionRepo = view.findViewById(R.id.descriptionRepo)
                languageRepo = view.findViewById(R.id.languageChip)
                lastUpdatedRepo = view.findViewById(R.id.lastUpdatedRepo)
                ownerRepo = view.findViewById(R.id.ownerRepo)
                isPrivateRepos = view.findViewById(R.id.isPrivateIcon)
            }
        }
    private fun setVisibilityWithData(view: View, data: String?) {
        if (data.isNullOrEmpty()) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            (view as TextView).text = data
        }
    }
}