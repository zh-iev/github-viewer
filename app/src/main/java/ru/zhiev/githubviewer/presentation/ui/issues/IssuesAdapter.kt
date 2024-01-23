package ru.zhiev.githubviewer.presentation.ui.issues

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.domain.models.IssueModel

class IssuesAdapter(
    private val context: Context,
    private var dataSet: List<IssueModel>,
    private val onItemClick: (IssueModel) -> Unit
) :
    RecyclerView.Adapter<IssuesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.issue_rv_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val issue = dataSet[position]
        viewHolder.titleIssue.text = issue.title
        viewHolder.repositoryName.text = "${context.getString(R.string.repository)}: ${issue.repository.name}"
        viewHolder.stateIssue.text = "${context.getString(R.string.state)}: ${issue.state}"

        viewHolder.itemView.setOnClickListener {
            onItemClick(issue)
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleIssue: TextView
        val repositoryName: TextView
        val stateIssue: Chip

        init {
            titleIssue = view.findViewById(R.id.titleIssue)
            repositoryName = view.findViewById(R.id.repositoryName)
            stateIssue = view.findViewById(R.id.stateIssue)
        }
    }
}
