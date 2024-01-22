package ru.zhiev.githubviewer.presentation.ui.repositories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.domain.models.GitHubRepositoryModel
import ru.zhiev.githubviewer.domain.models.RepositoryModel

class RepositoriesAdapter(
    private val dataSet: List<GitHubRepositoryModel>,
    private val onItemClick: (GitHubRepositoryModel) -> Unit)
    : RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_repositoties, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val repository = dataSet[position]
            viewHolder.nameRepo.text = repository.name
            viewHolder.descriptionRepo.text = repository.description
            viewHolder.languageRepo.text = repository.language
            viewHolder.lastUpdatedRepo.text = repository.pushedAt
            viewHolder.ownerRepo.text = repository.owner.login
            viewHolder.avatarRepo = repository.owner.avatarURL
            Glide.with(viewHolder.avatarRepo)
                .load(owner.avatarUrl)
                .into(viewHolder.avatarRepo)

            viewHolder.itemView.setOnClickListener {
                onItemClick(repository)
            }
        }
        override fun getItemCount() = dataSet.size
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val avatarRepo: ImageView
            val nameRepo: TextView
            val descriptionRepo: TextView
            val languageRepo: TextView
            val lastUpdatedRepo: TextView
            val ownerRepo: TextView

            init {
                avatarRepo = view.findViewById(R.id.avatarRepo)
                nameRepo = view.findViewById(R.id.nameRepo)
                descriptionRepo = view.findViewById(R.id.descriptionRepo)
                languageRepo = view.findViewById(R.id.languageRepo)
                lastUpdatedRepo = view.findViewById(R.id.lastUpdatedRepo)
                ownerRepo = view.findViewById(R.id.ownerRepo)
            }
        }
}