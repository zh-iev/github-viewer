package ru.zhiev.githubviewer.presentation.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.zhiev.githubviewer.R
import ru.zhiev.githubviewer.domain.models.UserModel

class UsersAdapter(
    private var dataSet: List<UserModel>,
    private val onItemClick: (UserModel) -> Unit
) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.user_rv_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = dataSet[position]
        viewHolder.userName.text = user.login
        Glide.with(viewHolder.avatar)
            .load(user.avatarUrl)
            .into(viewHolder.avatar)

        viewHolder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView
        val avatar: ImageView

        init {
            userName = view.findViewById(R.id.userNameList)
            avatar = view.findViewById(R.id.avatar)
        }
    }
}
